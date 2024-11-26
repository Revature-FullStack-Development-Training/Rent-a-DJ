package com.revature.controllers;

import com.revature.models.DJ;
import com.revature.models.DTOs.OutgoingReservationDTO;
import com.revature.models.Reservation;
import com.revature.models.User;
import com.revature.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/reservations")
@CrossOrigin
public class ReservationController {

    private static final Logger logger = LoggerFactory.getLogger(ReservationController.class);

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<OutgoingReservationDTO> addReservation(
            @RequestBody OutgoingReservationDTO newReservation) {
        OutgoingReservationDTO rDTO = reservationService.addReservation(
                newReservation.getLocation(),
                newReservation.getStartdatetime().toString(),
                newReservation.getEnddatetime().toString(),
                newReservation.getDjId(),
                newReservation.getUserId(),
                newReservation.getStatus());

        return ResponseEntity.ok(newReservation);
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

    @PatchMapping("{reservationId}/status")
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

    @GetMapping("/pending/dj/{djId}")
    public ResponseEntity<List<Reservation>> getPendingReservationsByDj(@PathVariable int djId) {
        // Retrieve the DJ object by ID (you may need a service method to fetch the DJ)
        DJ dj = reservationService.getDjById(djId);  // Assuming a method in the service layer to get the DJ by ID

        if (dj == null) {
            logger.error("DJ with ID: {} not found", djId);
            return ResponseEntity.status(404).body(Collections.emptyList());  // Return 404 if DJ not found
        }

        // Call the service to retrieve pending reservations by DJ
        List<Reservation> pendingReservations = reservationService.getPendingReservationsByDj(dj);

        // Log and return the result
        if (pendingReservations.isEmpty()) {
            logger.info("No pending reservations found for DJ ID: {}", djId);
            return ResponseEntity.status(204).body(pendingReservations);  // Return 204 No Content if empty
        }

        logger.info("Retrieved {} pending reservations for DJ ID: {}", pendingReservations.size(), djId);
        return ResponseEntity.ok(pendingReservations);  // Return 200 OK with the reservations
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        logger.error("IllegalArgumentException: {}", e.getMessage(), e);
        return ResponseEntity.status(400).body(e.getMessage());
    }
}
