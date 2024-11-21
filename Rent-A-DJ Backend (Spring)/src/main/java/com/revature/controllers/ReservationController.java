package com.revature.controllers;


import com.revature.models.Reservation;
import com.revature.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
//
//    @Autowired
//    private ReservationService reservationService;
//
//    @PostMapping
//    public ResponseEntity<Reservation> addReservation(@RequestBody Reservation newReservation) {
//        Reservation r = reservationService.addReservation(newReservation);
//        return ResponseEntity.status(201).body(r);
//    }
}
