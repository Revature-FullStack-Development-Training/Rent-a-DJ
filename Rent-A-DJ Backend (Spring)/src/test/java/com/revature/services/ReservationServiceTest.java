package com.revature.services;

import com.revature.daos.ReservationDAO;
import com.revature.daos.UserDAO;
import com.revature.models.DJ;
import com.revature.models.Reservation;
import com.revature.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @InjectMocks
    private ReservationService underTest;  // Inject mocks into the service

    @Mock
    private ReservationDAO rDAO;

    @Mock
    private UserDAO uDAO;

    @BeforeEach
    void setup() {
        // Inject both DAOs (ReservationDAO and UserDAO)
        underTest = new ReservationService(rDAO, uDAO);
    }

    @Test
    void canAddReservationSuccessfullyTest() {
        // Given
        String username = "existingUser";
        User mockUser = new User(1, "John", "Doe", username, "password", "user");
        DJ mockDJ = new DJ(1, "DJ Name", "DJ Last Name", "djUsername", "password", 100.0);
        String location = "Venue 1";
        String startdatetime = "2024-12-01T10:00:00"; // Ensure this includes seconds
        String enddatetime = "2024-12-01T12:00:00"; // Ensure this includes seconds
        String status = "Confirmed";

        Reservation newReservation = new Reservation(location, startdatetime, enddatetime, mockDJ, mockUser, status);

        // Mock the behavior of UserDAO to return the existing user
        when(uDAO.findByUsername(username)).thenReturn(mockUser);
        // Mock the behavior of ReservationDAO to return the saved reservation
        when(rDAO.save(any(Reservation.class))).thenReturn(newReservation);

        // When
        Reservation result = underTest.addReservation(location, startdatetime, enddatetime, mockDJ, mockUser, status);

        // Then
        assertNotNull(result); // Ensure the reservation is returned
        assertEquals(mockUser, result.getUser()); // Ensure the user is set correctly in the reservation
        assertEquals(location, result.getLocation()); // Ensure location is set correctly
        assertEquals(LocalDateTime.parse(startdatetime), result.getStartdatetime());  // Directly compare LocalDateTime
        assertEquals(LocalDateTime.parse(enddatetime), result.getEnddatetime());  // Directly compare LocalDateTime
        assertEquals(status, result.getStatus()); // Ensure status is correct

        verify(uDAO, times(1)).findByUsername(username); // Ensure findByUsername was called
        verify(rDAO, times(1)).save(any(Reservation.class)); // Ensure the save method was called
    }

    @Test
    void cannotAddReservationWhenUserNotFoundTest() {
        // Given
        String username = "nonexistentUser";
        User mockUser = new User(1, "John", "Doe", username, "password", "user");
        String location = "Venue 1";
        String startdatetime = "2024-12-01T10:00:00";
        String enddatetime = "2024-12-01T12:00:00";
        DJ mockDJ = new DJ(1, "DJ Name", "DJ Last Name", "djUsername", "password", 100.0);
        String status = "Confirmed";

        // Mock the behavior of UserDAO to return null for a non-existent user
        when(uDAO.findByUsername(username)).thenReturn(null);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> underTest.addReservation(location, startdatetime, enddatetime, mockDJ, mockUser, status));
        verify(uDAO, times(1)).findByUsername(username); // Ensure findByUsername was called
        verify(rDAO, never()).save(any(Reservation.class)); // Ensure the reservation is not saved
    }


}