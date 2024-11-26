package com.revature.services;

import com.revature.daos.UserDAO;
import com.revature.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    void canGetUserByUsernameTest() { // Tests getUserByUsername method
        // Test should pass if User is found with username
        String username = "user123";

        // Create a single user to test
        User user = new User(1, "John", "Doe", "user123", "password", "default user");

        // Mock the DAO method to return a single user
        when(userDAO.findByUsername(username)).thenReturn(user);

        // Act: Call the method with a valid username
        User result = underTest.getUserByUsername(username);

        // Assert: Ensure the result matches the mock
        assertEquals("user123", result.getUsername());  // The username should match the mocked value
        assertEquals("John", result.getFirstName());    // Check the first name of the returned user
        assertEquals("Doe", result.getLastName());      // Check the last name of the returned user

        verify(userDAO).findByUsername(username);  // Verify the DAO method was called
    }

    @Test
    void getUserByUsernameThrowsExceptionWhenUsernameIsBlankTest() {// Tests getUserByUsername method
        // Test when the username is blank
        String username = "";

        // Act & Assert: Assert that an exception is thrown
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> underTest.getUserByUsername(username));

        // Assert: Check the exception message
        assertEquals("Please search for a valid username!", exception.getMessage());

        // Verify the DAO method was NOT called
        verify(userDAO, never()).findByUsernameStartingWith(username);
    }

    @Test
    void canRegisterUserTest() {
        // Arrange: Create user to register
        User newUser = new User(0, "Test", "User", "testUser", "password123", "default user");
        User savedUser = new User(1, "Test", "User", "testUser", "password123", "default user");

        // Simulate checking if the username already exists (returns null for no match)
        when(userDAO.findByUsername(newUser.getUsername())).thenReturn(null);

        // Simulate saving the user
        when(userDAO.save(argThat(user ->
                user.getFirstName().equals(newUser.getFirstName()) &&
                        user.getLastName().equals(newUser.getLastName()) &&
                        user.getUsername().equals(newUser.getUsername()) &&
                        user.getPassword().equals(newUser.getPassword()) &&
                        user.getRole().equals(newUser.getRole())
        ))).thenReturn(savedUser);

        // Act: Register user
        User result = underTest.registerUser(
                newUser.getFirstName(), newUser.getLastName(),
                newUser.getUsername(), newUser.getPassword(),
                newUser.getRole());

        // Assert: Verify correct behavior
        assertEquals(savedUser, result); // Ensure the result matches the expected saved user
        verify(userDAO).findByUsername(newUser.getUsername()); // Check that username lookup was called
        verify(userDAO).save(any(User.class)); // Check that save was called
    }

    @Test
    void registerUserWithBlankUsernameThrowsExceptionTest() { // Tests registerUser method
        // Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> underTest.registerUser("Test", "User", "", "password123", "default user"));

        // Assert - Verify the exception message
        assertEquals("Username cannot be empty!", exception.getMessage());

        verify(userDAO, never()).findByUsername(anyString()); // Verify no database call for username check
        verify(userDAO, never()).save(any(User.class)); // Verify save was NOT called
    }

    @Test
    void canDeleteUserTest() { // Tests deleteUser method
        // id to delete
        int id = 1;

        // Mock the user to simulate its existence before deletion
        User existingUser = new User(1, "John", "Doe", "user123", "password", "default user");
        when(userDAO.findById(id)).thenReturn(Optional.of(existingUser)); // Mock the findById method of the DAO to return an Optional containing an existing user
        doNothing().when(userDAO).deleteById(id); //Mocks deleteById method of the DAO to do nothing when it's called.

        // Act
        User result = underTest.deleteUser(id); // Stores the deleted user in variable so we can compare in the AssertEquals method.

        // Assert
        assertEquals(existingUser, result); // Verify the deleted user is returned
        verify(userDAO).findById(id); // Ensure findById is called
        verify(userDAO).deleteById(id); // Ensure deleteById is called with correct ID
    }

    @Test
    void deletingUserWithInvalidIdThrowsExceptionTest() { // Tests deleteUser method
        // Arrange
        int invalidId = -1;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> underTest.deleteUser(invalidId));

        assertEquals("ID must be greater than 0.", exception.getMessage()); // Verify correct error was displayed.
    }

    @Test
    void canGetUserByUsernameStartingWithFoundUserTest() { // Tests getUserByUsernameStartingWith method
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
    void shouldThrowExceptionIfNoUsersFoundTest() { // Tests getUserByUsernameStartingWith method
        // Tests the behavior when no users are found starting with the given username:
        // It should throw an IllegalArgumentException.
        String username = "nonexistent";

        when(userDAO.findByUsernameStartingWith(username)).thenReturn(null); // Mock DAO to return null

        assertThrows(IllegalArgumentException.class, () -> {
            underTest.getUserByUsernameStartingWith(username); // Expect exception to be thrown
        });
    }

    @Test
    void canGetAllUsersTest() {
        // Act: Call the method being tested
        underTest.getAllUsers();

        // Assert: Verify that the UserDAO's findAll() method was called.
        verify(userDAO).findAll();
    }

    @Test
    void canGetAllUsersWithDataTest() {
        // Create a list of mock users
        List<User> mockUsers = List.of(
                new User(1, "John", "Doe", "user123", "password", "default user"),
                new User(2, "Jane", "Smith", "user456", "password", "default user")
        );

        // Mock the DAO to return the list of users
        when(userDAO.findAll()).thenReturn(mockUsers);

        // Act: Call the method being tested
        List<User> result = underTest.getAllUsers();

        // Assert: Ensure the list is not empty and contains the correct number of users
        assertFalse(result.isEmpty(), "The list should not be empty.");
        assertEquals(2, result.size(), "The list should contain two users.");
        assertEquals("John", result.get(0).getFirstName(), "The first user's name should be John.");
        assertEquals("Jane", result.get(1).getFirstName(), "The second user's name should be Jane.");

        // Verify that findAll was called
        verify(userDAO).findAll();
    }

    @Test
    void canChangeUsernameTest() { // Tests changeUsername method
        // Given: A user with an existing ID and valid new username
        int userId = 1;
        String newUsername = "newUsername";
        User existingUser = new User(userId, "currentUsername", "First", "currentUsername", "password", "user");

        // Mock the DAO to return the existing user
        when(userDAO.findByUserId(userId)).thenReturn(existingUser);
        when(userDAO.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Return the updated user

        // When: changeUsername is called
        User updatedUser = underTest.changeUsername(userId, newUsername);

        // Then: Verify the username is updated
        assertEquals(newUsername, updatedUser.getUsername());
        verify(userDAO).save(updatedUser);
    }

    @Test
    void cannotChangeUsernameWhenUserNotFoundTest() { // Tests changeUsername method
        // Given: A non-existent user ID
        int userId = 9999;
        String newUsername = "newUsername";

        // Mock the DAO to return null
        when(userDAO.findByUserId(userId)).thenReturn(null);

        // When: changeUsername is called
        Exception exception = assertThrows(IllegalArgumentException.class, () -> underTest.changeUsername(userId, newUsername));

        // Then: Verify the exception message
        assertEquals("User with User ID: " + userId + " was not found!", exception.getMessage());
        verify(userDAO, never()).save(any(User.class)); // Ensure save is not called
    }

    @Test
    void canChangePasswordTest() { // Tests changePassword method
        // Given: A valid user and new password
        int userId = 1;
        String newPassword = "newSecurePassword";
        User existingUser = new User(userId, "username", "First", "username", "oldPassword", "user");

        // Mock the DAO to return the existing user
        when(userDAO.findByUserId(userId)).thenReturn(existingUser);
        when(userDAO.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Return the updated user

        // When: changePassword is called
        User updatedUser = underTest.changePassword(userId, newPassword);

        // Then: Verify the password is updated
        assertEquals(newPassword, updatedUser.getPassword());
        verify(userDAO).save(updatedUser); // Ensure save is called
    }

    @Test
    void cannotChangePasswordWhenUserNotFoundTest() { // Tests changePassword method
        // Given: A non-existent user ID and a new password
        int userId = 99;
        String newPassword = "newSecurePassword";

        // Mock the DAO to return null
        when(userDAO.findByUserId(userId)).thenReturn(null);

        // When: changePassword is called
        Exception exception = assertThrows(IllegalArgumentException.class, () -> underTest.changePassword(userId, newPassword));

        // Then: Verify the exception message
        assertEquals("User with User ID: " + userId + " was not found!", exception.getMessage());
        verify(userDAO, never()).save(any(User.class)); // Ensure save is not called
    }
}