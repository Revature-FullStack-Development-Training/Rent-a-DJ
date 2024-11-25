package com.revature.services;

import com.revature.daos.UserDAO;
import com.revature.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Enables Mockito in test class, allowing the use of @Mock and @InjectMocks annotations.
public class UserServiceTest {

    private UserService underTest; // creates instance of UserService to test

    @Mock // creates a Mock of the UserDAO for testing
    private UserDAO userDAO;

    @BeforeEach // Runs before each test to initialize the UserService w/ the mocked DAO.
    void setUp(){
        underTest = new UserService(userDAO);
    }

    @Test
    void getUserByUsername() {

    }

    @Test
    void registerUser() {
    }

    @Test
    void deleteUser() {
    }



    @Test
    void canGetUserByUsernameStartingWithFoundUser() {
        // Test should pass if User is found with username
        String username = "user";

        // Create list with users to test
        List<User> users = List.of(
                new User(1, "John", "Doe", "user123", "password", "default user"),
                new User(2, "Jane", "Smith", "user456", "password", "default user")
        );

        // Mock the DAO method to return a list of users
        when(userDAO.findByUsernameStartingWith(username)).thenReturn(users);

        // Act: Call the method with a valid username prefix
        List<User> result = underTest.getUserByUsernameStartingWith(username);

        // Assert: Ensure the result matches the mock
        assertEquals(2, result.size()); // Should return 2 users
        assertTrue(result.get(0).getUsername().startsWith(username));
        assertTrue(result.get(1).getUsername().startsWith(username));

        verify(userDAO).findByUsernameStartingWith(username); // Verify the DAO method was called
    }

    @Test
    void canGetAllUsers() {
        // Act: Call the method being tested
        underTest.getAllUsers();

        // Assert: Verify that the UserDAO's findAll() method was called.
        verify(userDAO).findAll();
    }
}