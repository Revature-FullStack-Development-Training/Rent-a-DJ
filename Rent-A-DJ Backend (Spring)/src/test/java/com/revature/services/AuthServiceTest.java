package com.revature.services;

import com.revature.daos.AuthDAO;
import com.revature.models.DTOs.LoginDTO;
import com.revature.models.DTOs.OutgoingUserDTO;
import com.revature.models.User;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Enables Mockito in test class, allowing the use of @Mock and @InjectMocks annotations.
public class AuthServiceTest {

    @Mock
    private AuthDAO aDAO;  // Mock the AuthDAO

    @Mock
    private HttpSession session;  // Mock the HttpSession

    @InjectMocks
    private AuthService authService;  // Inject the mocks into AuthService

    private LoginDTO loginDTO;

    @BeforeEach
    void setUp() {
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
}