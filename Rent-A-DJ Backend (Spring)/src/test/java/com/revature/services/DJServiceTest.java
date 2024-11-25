package com.revature.services;

import com.revature.daos.DJDAO;
import com.revature.models.DJ;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void getAllDJs() {
    }

    @Test
    void changePassword() {
    }

    @Test
    void removeDJ() {
    }
}