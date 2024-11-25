package com.revature.controllers;

import com.revature.models.DJ;
import com.revature.models.Reservation;
import com.revature.models.User;
import com.revature.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    // Method to add a reservation with specific parameters
    @PostMapping
    public ResponseEntity<Reservation> addReservation(
            @RequestParam String location,
            @RequestParam String startdatetime,
            @RequestParam String enddatetime,
            @RequestParam DJ dj,
            @RequestParam User user,
            @RequestParam String status) {

        // Call the service to add the reservation
        Reservation newReservation = reservationService.addReservation(location, startdatetime, enddatetime, dj, user, status);


        return ResponseEntity.status(201).body(newReservation);
    }


    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> allReservations = reservationService.getAllReservations();
        return ResponseEntity.ok(allReservations);
    }

    // Get all reservations for a specific user
    @GetMapping("/user/{username}")
    public ResponseEntity<List<Reservation>> getUserReservations(@PathVariable String username) {
        List<Reservation> userReservations = reservationService.getUserReservations(username);
        return ResponseEntity.ok(userReservations);
    }

    // Get all pending reservations for a specific user
    @GetMapping("/user/{username}/pending")
    public ResponseEntity<List<Reservation>> getPendingUserReservations(@PathVariable String username) {
        List<Reservation> pendingUserReservations = reservationService.getPendingUserReservations(username);
        return ResponseEntity.ok(pendingUserReservations);
    }

    // Get all pending reservations
    @GetMapping("/pending")
    public ResponseEntity<List<Reservation>> getPendingReservations() {
        List<Reservation> pendingReservations = reservationService.getAllPendingReservations();
        return ResponseEntity.ok(pendingReservations);
    }
}
