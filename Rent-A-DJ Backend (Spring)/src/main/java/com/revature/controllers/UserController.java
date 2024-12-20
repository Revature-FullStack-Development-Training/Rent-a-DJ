package com.revature.controllers;


import com.revature.daos.UserDAO;
import com.revature.models.DJ;
import com.revature.models.User;
import com.revature.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private UserService userService;

    private UserDAO uDAO;

    @Autowired
    public UserController(UserService userService, UserDAO uDAO){
        this.userService = userService;
        this.uDAO = uDAO;
    }

    @PostMapping
    public ResponseEntity<User> registerUser(@RequestBody User newUser) {
        User u = userService.registerUser(newUser.getFirstName(), newUser.getLastName(), newUser.getUsername(), newUser.getPassword(), newUser.getRole());

        logger.info("Successfully registered user: {}", u.getUsername());
        return ResponseEntity.status(201).body(u);
    }
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {

        List<User> allUsers = userService.getAllUsers();

        logger.info("Retrieved {} users", allUsers.size());
        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/username")
    public ResponseEntity<User> getUserByUsername(@RequestParam String username) {
        User u = userService.getUserByUsername(username);

        logger.info("Found user: {}", u.getUsername());
        return ResponseEntity.ok((User) u);
    }

    @PatchMapping("/users/{username}")
    public ResponseEntity<User> getUserByUsernameStartingWith(@PathVariable String username) {
        List<User> u = userService.getUserByUsernameStartingWith(username);

        return ResponseEntity.ok((User) u);
    }

    //handles password changes for the User
    @PatchMapping("{userId}/password")
    public ResponseEntity<User> changePassword(@PathVariable int userId, @RequestBody String password){
        User user = userService.changePassword(userId, password);

        logger.info("Successfully changed password for user ID: {}", userId);
        return ResponseEntity.ok(user);
    }

    //handles username changes for the User
    @PatchMapping("{userId}/username")
    public ResponseEntity<User> changeUsername(@PathVariable int userId, @RequestBody String password){
        User user = userService.changeUsername(userId, password);

        logger.info("Successfully changed username for user ID: {}", userId);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{username}")
    public ResponseEntity<User> deleteUser(@PathVariable String username) {
        User userToDelete = userService.getUserByUsername(username);

        userService.deleteUser(userToDelete.getUserId());

        logger.info("Successfully deleted user: {}", username);
        return ResponseEntity.ok(userToDelete);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        logger.error("IllegalArgumentException: {}", e.getMessage(), e);
        return ResponseEntity.status(400).body(e.getMessage());
    }
}
