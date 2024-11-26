package com.revature.services;

import com.revature.daos.AuthDAO;
import com.revature.daos.DJDAO;
import com.revature.models.DTOs.LoginDTO;
import com.revature.models.DTOs.OutgoingUserDTO;
import com.revature.models.User;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Enables Mockito in test class, allowing the use of @Mock and @InjectMocks annotations.
public class AuthServiceTest {

    @Mock
    private AuthDAO aDAO;  // Mock the AuthDAO

    @Mock
    private DJDAO djDAO; // Mock the DJDAO (used by DJService)

    @Mock
    private HttpSession session;  // Mock the HttpSession

    @InjectMocks
    private AuthService authService;  // Inject the mocks into AuthService

    @Mock
    private DJService djService; // Mock the DJService

    private LoginDTO loginDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        loginDTO = new LoginDTO("testUser", "password123"); // Sample login data
    }

    @Test
    void testLoginSuccess() {
        // Given: A user is found by username and password
        User user = new User(1, "testUser", "Test", "testUser", "password123", "default user");
        when(aDAO.findByUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword())).thenReturn(user);

        // When: The login method is called
        OutgoingUserDTO result = authService.login(loginDTO, session);

        // Then: Verify that the session attributes are set
        verify(session).setAttribute("userId", user.getUserId());
        verify(session).setAttribute("username", user.getUsername());
        verify(session).setAttribute("role", user.getRole());

        // And: Verify that the returned DTO contains the correct user information
        assertEquals(user.getUserId(), result.getUserId());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getRole(), result.getRole());
    }

    @Test
    void testLoginWithInvalidCredentials() {
        // Given: A LoginDTO with invalid username and password
        LoginDTO invalidLoginDTO = new LoginDTO("invalidUser", "wrongPassword");

        // Mock the DAO to return null when looking up the invalid credentials
        when(aDAO.findByUsernameAndPassword(invalidLoginDTO.getUsername(), invalidLoginDTO.getPassword())).thenReturn(null);

        // Mock the DJ service to return null when looking up the invalid username
        when(djService.findByUsername(invalidLoginDTO.getUsername())).thenReturn(null);

        // When: The login method is called with invalid credentials
        Exception exception = assertThrows(IllegalArgumentException.class, () -> authService.login(invalidLoginDTO, session));

        // Then: Ensure the exception message is the expected one
        assertEquals("No user or DJ found with those credentials!", exception.getMessage());

        // Verify that session was not modified since the login failed
        verify(session, times(0)).setAttribute(anyString(), any());
    }
}