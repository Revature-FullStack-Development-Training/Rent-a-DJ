package com.revature.controllers;

import com.revature.models.DTOs.LoginDTO;
import com.revature.models.DTOs.OutgoingUserDTO;
import com.revature.services.AuthService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin //requests from any location can react this no problem. not very secure
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    //autowire the service
    AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //uninitialized HttpSession (filled upon successful login)
    public static HttpSession session;

    //NOTE: our HTTP Session is coming in via parameters this time (to be sent to the controller)
    //The session in the parameter is not the same as the static session above
    @PostMapping
    public ResponseEntity<OutgoingUserDTO> login(@RequestBody LoginDTO lDTO, HttpSession session){

        try {
            // Send LoginDTO to service, getting us the OutUser
            OutgoingUserDTO uDTO = authService.login(lDTO, session);

            // If we get here, login was successful and session was created!
            logger.info("Login successful for user: {}", lDTO.getUsername()); // Log success

            return ResponseEntity.ok(uDTO);

        } catch (IllegalArgumentException e) {
            logger.error("Login failed for user: {}. Error: {}", lDTO.getUsername(), e.getMessage()); // Log error if login fails
            throw e; // Rethrow the exception to be caught by the @ExceptionHandler
        }

    }

    //Exception Handler (stole this from the UserController)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e){
        logger.error("Handled IllegalArgumentException: {}", e.getMessage());
        //Return a 400 (BAD REQUEST) status code with the exception message
        return ResponseEntity.status(400).body(e.getMessage());
    }

}
