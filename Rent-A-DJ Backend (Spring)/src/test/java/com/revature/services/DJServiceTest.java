package com.revature.services;

import com.revature.daos.DJDAO;
import com.revature.models.DJ;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Enables Mockito in test class, allowing the use of @Mock and @InjectMocks annotations.
public class DJServiceTest {

    private DJService underTest;

    @Mock
    private DJDAO dDAO;

    @BeforeEach
        // Runs before each test to initialize the UserService w/ the mocked DAO.
    void setUp(){

        underTest = new DJService(dDAO);
    }

    @Test
    void successfulFindByUsernameTest() { // Tests findByUsername method

        // Given: A DJ object with a known username
        DJ expectedDJ = new DJ(1, "John", "Doe", "dj123", "password123", 50.0);

        // Mock the DAO method to return the DJ object when called with the specific username
        when(dDAO.findByUsername("dj123")).thenReturn(Optional.of(expectedDJ));

        // When: Call the method being tested
        DJ result = underTest.findByUsername("dj123");

        // Then: Verify that the correct DJ is returned
        assertEquals(expectedDJ, result); // Ensure that the returned DJ is the one we mocked
    }

    @Test
    void findByUsernameWhenUsernameNotFoundTest() { // Tests findByUsername method
        // Given: A username that does not exist in the database
        String invalidUsername = "nonexistentDJ";

        // Mock the DAO method to return an empty Optional when called with the invalid username
        when(dDAO.findByUsername(invalidUsername)).thenReturn(Optional.empty());

        // When: Call the method being tested
        // Then: Expect an exception to be thrown
        assertThrows(IllegalArgumentException.class, () -> {
            underTest.findByUsername(invalidUsername);
        }); // Confirms no User was found in db with that username, and that IllegalArgumentException was successfully thrown.
    }

    @Test
    void canSuccessfullyRegisterDJTest() {
        // Given: A valid DJ registration request
        String firstName = "John";
        String lastName = "Doe";
        String username = "username";
        String password = "password";
        double rate = 50.0;

        DJ newDJ = new DJ(0, firstName, lastName, username, password, rate);

        // When: We mock the dDAO to not find any DJ with the given username
        when(dDAO.findByUsername(username)).thenReturn(Optional.empty());
        when(dDAO.save(any(DJ.class))).thenReturn(newDJ);

        // Then: The registerDJ method should save the DJ and return the saved DJ
        DJ result = underTest.registerDJ(firstName, lastName, username, password, rate);

        assertNotNull(result);
        assertEquals(firstName, result.getFirstName());
        assertEquals(lastName, result.getLastName());
        assertEquals(username, result.getUsername());
        assertEquals(password, result.getPassword());
        assertEquals(rate, result.getRate(), 0);

        // Verify that the findByUsername method was called once
        verify(dDAO).findByUsername(username);
        verify(dDAO).save(any(DJ.class));
    }

    @Test
    void cannotRegisterDJWhenUsernameAlreadyExistsTest() {
        // Given: A DJ registration with a username that already exists
        String firstName = "John";
        String lastName = "Doe";
        String username = "username";
        String password = "password";
        double rate = 50.0;

        DJ existingDJ = new DJ(1, "Jane", "Doe", "username", "password", 100.0);

        // When: We mock the dDAO to return an existing DJ with the given username
        when(dDAO.findByUsername(username)).thenReturn(Optional.of(existingDJ));

        // Then: The registerDJ method should throw an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            underTest.registerDJ(firstName, lastName, username, password, rate);
        });

        // Verify that the findByUsername method was called once
        verify(dDAO).findByUsername(username);
        verify(dDAO, times(0)).save(any(DJ.class)); // save should not be called
    }

    @Test
    void canSuccessfullyChangeRateTest() { // Tests changeRate method
        // Given: A DJ with ID 1 exists in the database
        int djId = 1;
        Double newRate = 100.0;
        DJ existingDJ = new DJ(djId, "John", "Doe", "username", "password", 50.0);

        // When: The DJ is found in the database, and the rate is updated
        when(dDAO.findByDjId(djId)).thenReturn(existingDJ);
        when(dDAO.save(any(DJ.class))).thenReturn(existingDJ);

        // Then: The rate should be updated and the updated DJ should be returned
        DJ result = underTest.changeRate(djId, newRate);

        assertNotNull(result);
        assertEquals(newRate, result.getRate(), 0); // 0 here is a delta value, which is used to specify the amount of variance allowed when comparing floating point numbers (doubles).

        // Verify that the findByDjId method and save method were called
        verify(dDAO).findByDjId(djId);
        verify(dDAO).save(any(DJ.class));
    }

    @Test
    void newPayRateCannotBeZeroTest() { // Tests changeRate method
        // Given: A DJ with a rate of 50.0
        int djId = 1;
        Double newRate = 0.0; // Test case where new rate is 0
        DJ existingDJ = new DJ(djId, "John", "Doe", "username", "password", 50.0); // rate is 50

        // When: The DJ is found in the database, and we try to change the rate to 0.0
        when(dDAO.findByDjId(djId)).thenReturn(existingDJ);

        // Then: The method should throw an IllegalArgumentException because the new rate cannot be 0
        assertThrows(IllegalArgumentException.class, () -> {
            underTest.changeRate(djId, newRate);
        });

        // Verify that the findByDjId method was called once
        verify(dDAO).findByDjId(djId);
        verify(dDAO, times(0)).save(any(DJ.class)); // save should not be called
    }

    @Test
    void canSuccessfullyGetALlDJsTest() { // Tests getAllDJs method
        // Given: A list of DJs returned by the DAO
        List<DJ> mockDJs = List.of(
                new DJ(1, "John", "Doe", "johnny", "password", 50.0),
                new DJ(2, "Jane", "Smith", "janey", "password", 60.0)
        );
        when(dDAO.findAll()).thenReturn(mockDJs);

        // When: getAllDJs is called
        List<DJ> result = underTest.getAllDJs();

        // Then: Verify the returned list
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(mockDJs, result);
        verify(dDAO, times(1)).findAll(); // Ensure the DAO method was called once
    }

    @Test
    void testGetAllDJsEmptyList() { // Tests getAllDJs method
        // Given: An empty list is returned by the DJDAO
        List<DJ> emptyList = new ArrayList<>();

        when(dDAO.findAll()).thenReturn(emptyList); // Mock the behavior of the DAO to return an empty list

        // When: Calling the getAllDJs method
        List<DJ> result = underTest.getAllDJs();

        // Then: The returned list should be empty
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void canChangePasswordSuccessfullyTest() { // Test changePassword method
        // Given: A DJ object with the ID 1 and a new password
        DJ mockDJ = new DJ(1, "John", "Doe", "johnny", "oldPassword", 50.0);
        when(dDAO.findByDjId(1)).thenReturn(mockDJ);  // Mocking the DJ return
        String newPassword = "newPassword";  // New password to set
        when(dDAO.save(mockDJ)).thenReturn(mockDJ);  // Mocking the save method to return the updated DJ

        // When: Calling the changePassword method
        DJ result = underTest.changePassword(1, newPassword);

        // Then: Assert that the result is not null
        assertNotNull(result);  // Ensure the result is not null
        assertEquals(newPassword, result.getPassword());  // Check if the password was updated
        verify(dDAO, times(1)).save(mockDJ);  // Ensure the save method was called once
    }

    @Test
    void cannotChangePasswordWhenDJNotFoundTest() { // Test changePassword method
        // Given: The DJ does not exist
        when(dDAO.findByDjId(1)).thenReturn(null); // Mock the DAO to return null when searching for a non-existing DJ

        // When & Then: Calling changePassword should throw an IllegalArgumentException
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            underTest.changePassword(1, "newPassword123");
        });

        assertEquals("DJ with DJ ID: 1 was not found!", exception.getMessage());
    }

    @Test
    void canChangeUsernameSuccessfullyTest() { // Tests changeUsername method
        // Given: A DJ object with the ID 1 and a new username
        DJ mockDJ = new DJ(1, "John", "Doe", "username", "password", 50.0);
        when(dDAO.findByDjId(1)).thenReturn(mockDJ);  // Mocking the DJ return
        String newUsername = "newUsername";  // New username to set
        when(dDAO.save(mockDJ)).thenReturn(mockDJ);  // Mocking the save method to return the updated DJ

        // When: Calling the changeUsername method
        DJ result = underTest.changeUsername(1, newUsername);

        // Then: Assert that the result is not null and the username was updated
        assertNotNull(result);  // Ensure the result is not null
        assertEquals(newUsername, result.getUsername());  // Check if the username was updated
        verify(dDAO, times(1)).save(mockDJ);  // Ensure the save method was called once
    }

    @Test
    void cannotChangeUsernameWhenDJNotFoundTest() { // Tests changeUsername method
        // Given: No DJ exists with the given ID
        when(dDAO.findByDjId(1)).thenReturn(null);  // Mocking the DJ not found scenario

        // When: Calling the changeUsername method
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            underTest.changeUsername(1, "newUsername");
        });

        // Then: Verify that the exception message is as expected
        assertEquals("DJ with DJ ID: 1 was not found!", exception.getMessage());
    }

    @Test
    void canRemoveDJSuccessfullyTest() { // Tests removeDJ method
        // Given: A DJ object with the ID 1 that exists in the database
        DJ mockDJ = new DJ(1, "John", "Doe", "username", "password", 50.0);
        when(dDAO.findByDjId(1)).thenReturn(mockDJ); // Mock the DJ retrieval
        doNothing().when(dDAO).delete(mockDJ); // Mock the deletion process

        // When: Calling the removeDJ method
        DJ result = underTest.removeDJ(1);

        // Then: Assert that the DJ was deleted and returned
        assertNotNull(result);  // Ensure the result is not null
        assertEquals(mockDJ, result);  // Ensure the returned DJ is the same as the mock DJ
        verify(dDAO, times(1)).delete(mockDJ);  // Ensure the delete method was called once
    }

    @Test
    void cannotRemoveDJWhenInvalidIdTest() { // Tests removeDJ method
        // Given: An invalid DJ ID (e.g., 0)
        int invalidDjId = 0;

        // When & Then: Calling removeDJ should throw an IllegalArgumentException
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            underTest.removeDJ(invalidDjId);
        });

        // Then: Assert that the exception message is as expected
        assertEquals("DJ Id must be greater than 0!", exception.getMessage());
    }

}