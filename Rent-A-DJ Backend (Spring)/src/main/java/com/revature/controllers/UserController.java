package com.revature.controllers;


import com.revature.daos.UserDAO;
import com.revature.models.DJ;
import com.revature.models.User;
import com.revature.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

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
        return ResponseEntity.status(201).body(u);
    }
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {

        List<User> allUsers = userService.getAllUsers();

        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/username")
    public ResponseEntity<User> getUserByUsername(@RequestParam String username) {
        User u = userService.getUserByUsername(username);
        return ResponseEntity.ok((User) u);
    }

    @PatchMapping("/users/{username}")
    public ResponseEntity<User> getUserByUsernameStartingWith(@PathVariable String username) {
        List<User> u = userService.getUserByUsernameStartingWith(username);
        return ResponseEntity.ok((User) u);
    }

    //handles password changes for the User
    @PatchMapping("{userId}/password")
    public ResponseEntity<User> changePassword(@PathVariable int userId, String password){
        User user = userService.changePassword(userId, password);
        return ResponseEntity.ok(user);
    }

    //handles username changes for the User
    @PatchMapping("{userId}/username")
    public ResponseEntity<User> changeUsername(@PathVariable int userId, String password){
        User user = userService.changeUsername(userId, password);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{username}")
    public ResponseEntity<User> deleteUser(@PathVariable String username) {
        User userToDelete = userService.getUserByUsername(username);

        userService.deleteUser(userToDelete.getUserId());

        return ResponseEntity.ok(userToDelete);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }
}
