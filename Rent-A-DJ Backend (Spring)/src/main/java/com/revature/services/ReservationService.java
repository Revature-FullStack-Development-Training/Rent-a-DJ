package com.revature.services;

//Check UserService for general notes on Services

import com.revature.daos.DJDAO;
import com.revature.daos.ReservationDAO;
import com.revature.daos.UserDAO;
import com.revature.models.DJ;
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
    public ReservationService(ReservationDAO rDAO, UserDAO uDAO) {
        this.rDAO = rDAO;
        this.uDAO = uDAO;
        this.dDAO = dDAO;
    }

    //This method takes in a new Reservation object and inserts it into the DB
    public Reservation addReservation(String location, String startdatetime, String enddatetime, DJ dj, User user, String status) {

        //Another important role of the Service layer: data processing -
        //Turn the ReservationDTO into a Reservation to send to the DAO (DAO takes Reservation objects, not ReservationDTOs)

        //reservationId will be generated (so 0 is just a placeholder)
        //species and name come from the DTO
        //user will be set with the userId in the DTO
        Reservation newReservation = new Reservation(location, startdatetime, enddatetime, dj, user, status);

        // Use the UserDAO to get a User by username
        User foundUser = uDAO.findByUsername(user.getUsername());

        // Check if the user is null (not found)
        if (foundUser == null) {
            logger.error("No user found with username: {}", user.getUsername());
            throw new IllegalArgumentException("No user found with username: " + newReservation.getUser().getUsername());
        } else {
            // Set the user object in the new Reservation
            newReservation.setUser(foundUser);

            // Log and save the reservation
            logger.info("Successfully created reservation for user: {} at location: {} from {} to {}",
                    foundUser.getUsername(), location, startdatetime, enddatetime);

            // Send the Reservation to the DAO
            return rDAO.save(newReservation);
        }
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