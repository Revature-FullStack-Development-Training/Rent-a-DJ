package com.revature.controllers;

import com.revature.models.DJ;
import com.revature.models.Reservation;
import com.revature.models.User;
import com.revature.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reservations")
@CrossOrigin
public class ReservationController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

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

        logger.info("Successfully added reservation for user: {} with ID: {}", user.getUsername(), newReservation.getReservationId());

        return ResponseEntity.status(201).body(newReservation);
    }


    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> allReservations = reservationService.getAllReservations();

        logger.info("Retrieved {} reservations", allReservations.size()); // Log the number of reservations fetched
        return ResponseEntity.ok(allReservations);
    }

    // Get all reservations for a specific user
    @GetMapping("/user/{username}")
    public ResponseEntity<List<Reservation>> getUserReservations(@PathVariable String username) {
        List<Reservation> userReservations = reservationService.getUserReservations(username);

        logger.info("Retrieved {} reservations for user: {}", userReservations.size(), username);
        return ResponseEntity.ok(userReservations);
    }

    // Get all pending reservations for a specific user
    @GetMapping("/user/{username}/pending")
    public ResponseEntity<List<Reservation>> getPendingUserReservations(@PathVariable String username) {
        List<Reservation> pendingUserReservations = reservationService.getPendingUserReservations(username);

        logger.info("Retrieved {} pending reservations for user: {}", pendingUserReservations.size(), username);
        return ResponseEntity.ok(pendingUserReservations);
    }

    // Get all pending reservations
    @GetMapping("/pending")
    public ResponseEntity<List<Reservation>> getPendingReservations() {
        List<Reservation> pendingReservations = reservationService.getAllPendingReservations();

        logger.info("Retrieved {} pending reservations", pendingReservations.size());
        return ResponseEntity.ok(pendingReservations);
    }

    @GetMapping("/dj/{djId}/username/{username}")
    public ResponseEntity<List<Reservation>> getReservationsByDj(@PathVariable int djId, @PathVariable String username) {
        List<Reservation> reservations = reservationService.getReservationsByDjIdAndUsername(djId, username);

        logger.info("Retrieved {} reservations for DJ ID: {} and user: {}", reservations.size(), djId, username);
        return ResponseEntity.ok(reservations);
    }

    @PatchMapping("{reservationId}/status}")
    public ResponseEntity<Reservation> resolveReservation(@PathVariable int id, @RequestParam String status){
        Reservation reservation = reservationService.resolveReservation(id, status);

        return ResponseEntity.ok(reservation);
    }


    //Handles changing reservation start time
    @PatchMapping("/{reservationId}/startTime")
    public ResponseEntity<Reservation> changeStartTime(@PathVariable int reservationId, @RequestBody LocalDateTime newTime){
        Reservation res = reservationService.changeReservationStartTime(reservationId, newTime);
        return ResponseEntity.ok(res);
    }


    @PatchMapping("/{reservationId}/location")
    public ResponseEntity<Reservation> updateReservationLocation(@PathVariable int reservationId, @RequestBody String newLocation){
        Reservation reservation = reservationService.updateReservationLocation(reservationId, newLocation);

        return ResponseEntity.ok(reservation);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        logger.error("IllegalArgumentException: {}", e.getMessage(), e);
        return ResponseEntity.status(400).body(e.getMessage());
    }
}
