package com.revature.services;

//Check UserService for general notes on Services

import com.revature.daos.DJDAO;
import com.revature.daos.ReservationDAO;
import com.revature.daos.UserDAO;
import com.revature.models.DJ;
import com.revature.models.DTOs.OutgoingReservationDTO;
import com.revature.models.Reservation;
import com.revature.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service //Makes a class a bean. Stereotype annotation.
public class ReservationService {
    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    //autowire the ReservationDAO with constructor injection so we can use the ReservationDAO methods
    private ReservationDAO rDAO;
    private UserDAO uDAO;
    private DJDAO dDAO;//we also need some UserDAO methods!

    @Autowired
    public ReservationService(ReservationDAO rDAO, UserDAO uDAO, DJDAO dDAO) {
        this.rDAO = rDAO;
        this.uDAO = uDAO;
        this.dDAO = dDAO;
    }

    //This method takes in a new Reservation object and inserts it into the DB
    public OutgoingReservationDTO addReservation(String location, String startdatetime, String enddatetime, int djId, int userId, String status) {
        // Fetch the DJ and User from their respective DAOs
        DJ dj = dDAO.findById(djId).orElseThrow(() -> new RuntimeException("DJ not found with ID: " + djId));
        User user = uDAO.findById(userId).orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Create a new Reservation
        Reservation newReservation = new Reservation(location, startdatetime, enddatetime, dj, user, status);

        // Save the reservation
        Reservation savedReservation = rDAO.save(newReservation);

        // Return a DTO containing the relevant details
        return new OutgoingReservationDTO(
                savedReservation.getLocation(),
                savedReservation.getStartdatetime().toString(),
                savedReservation.getEnddatetime().toString(),
                savedReservation.getDj().getDjId(),
                savedReservation.getUser().getUserId(),
                savedReservation.getStatus()
        );
    }

    @Transactional
    public Reservation resolveReservation(int id, String status) {
        if (!(status.equalsIgnoreCase("pending") || status.equalsIgnoreCase("approved") || status.equalsIgnoreCase("denied"))) {
            throw new IllegalArgumentException("Invalid status");
        }

        Reservation r = rDAO.findByReservationId(id);

        if (r == null) {
            throw new IllegalArgumentException("No reservation found with id: " + id);
        } else {
            r.setStatus(status);
            return r;
        }
    }

    public List<Reservation> getUserReservations(String username) {
        Optional<User> u = Optional.ofNullable(uDAO.findByUsername(username));
        if (u.isEmpty()) {
            logger.error("User with Username: {} was not found when getting reservations",username);
            throw new IllegalArgumentException("No user found with username: " + username);
        } else {
            return rDAO.findByUser(u.get());
        }
    }

    public List<Reservation> getPendingUserReservations(String username) {
        Optional<User> u = Optional.ofNullable(uDAO.findByUsername(username));
        if (u.isEmpty()) {
            logger.error("User with Username: {} was not found when getting pending reservations", username);
            throw new IllegalArgumentException("No user found with username: " + username);
        } else {
            return rDAO.findByUserAndStatus(u.get(),"pending");
        }
    }

    public List<Reservation> getAllPendingReservations() {
        return rDAO.findByStatus("pending");
    }
    //This method gets all reservations from the DB
    public List<Reservation> getAllReservations() {
        //not much error handling in a get all
        return rDAO.findAll();
    }

    public List<Reservation> getReservationsByDjIdAndUsername(int djId, String username) {
        return rDAO.findByDj_DjIdAndDj_Username(djId, username);
    }

    public Reservation updateReservationLocation(int id, String newLocation){

        //Reservation object for return and error handling
        Optional<Reservation> r = Optional.ofNullable(rDAO.findByReservationId(id));

        //error handling
        if(r.isEmpty()){
            throw new IllegalArgumentException("Reservation with Reservation ID: " + id + " not found!");
        }
        else if (r.get().getLocation().equals(newLocation)) {
            throw new IllegalArgumentException("Location is already " + newLocation + "!");
        } else if (!r.get().getStatus().equalsIgnoreCase("pending")) {
            throw new IllegalArgumentException("Can only change the location of pending reservations!");
        }
        //any other error handling goes above here
        else {
            r.get().setLocation(newLocation);
            return rDAO.save(r.get());
        }
    }

    public Reservation changeReservationStartTime(int id, LocalDateTime newTime){
        //find reservation by id or throw IllegalArgumentException
        Reservation reservation = rDAO.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        reservation.setStartdatetime(newTime);
        return rDAO.save(reservation);
    }

    public List<Reservation> getPendingReservationsByDj(DJ dj) {
        // Check if the DJ exists (you may want to check this)
        if (dj == null) {
            logger.error("DJ object is null");
            throw new IllegalArgumentException("DJ cannot be null");
        }

        // Retrieve reservations where the DJ is assigned and the status is 'pending'
        List<Reservation> pendingReservations = rDAO.findByDj_DjIdAndStatus(dj, "pending");

        if (pendingReservations.isEmpty()) {
            logger.info("No pending reservations found for DJ: {}", dj.getFirstName());
        } else {
            logger.info("Found {} pending reservations for DJ: {}", pendingReservations.size(), dj.getFirstName());
        }

        return pendingReservations;
    }

    public DJ getDjById(int djId) {
        // Use findById() to get the DJ, which returns an Optional
        Optional<DJ> dj = dDAO.findById(djId);

        // Check if DJ exists, otherwise throw an exception or handle the missing DJ case
        if (dj.isPresent()) {
            return dj.get();  // Return the DJ if found
        } else {
            throw new IllegalArgumentException("DJ not found with ID: " + djId);  // Handle DJ not found
        }
    }
}