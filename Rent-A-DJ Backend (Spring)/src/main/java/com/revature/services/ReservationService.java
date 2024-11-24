package com.revature.services;

//Check UserService for general notes on Services

import com.revature.daos.ReservationDAO;
import com.revature.daos.UserDAO;
import com.revature.models.DJ;
import com.revature.models.Reservation;
import com.revature.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service //Makes a class a bean. Stereotype annotation.
public class ReservationService {

    //autowire the ReservationDAO with constructor injection so we can use the ReservationDAO methods
    private ReservationDAO rDAO;
    private UserDAO uDAO; //we also need some UserDAO methods!

    @Autowired
    public ReservationService(ReservationDAO rDAO, UserDAO uDAO) {
        this.rDAO = rDAO;
        this.uDAO = uDAO;
    }

    //This method takes in a new Reservation object and inserts it into the DB
    public Reservation addReservation(String location, String startdatetime, String enddatetime, DJ dj, User user, String status) {

        //Another important role of the Service layer: data processing -
        //Turn the ReservationDTO into a Reservation to send to the DAO (DAO takes Reservation objects, not ReservationDTOs)

        //reservationId will be generated (so 0 is just a placeholder)
        //species and name come from the DTO
        //user will be set with the userId in the DTO
        Reservation newReservation = new Reservation(location, startdatetime, enddatetime, dj, user, status);

        //Use the UserDAO to get a User by id
        Optional<User> u = Optional.ofNullable(uDAO.findByUsername(user.getUsername()).getFirst());

        /*findById returns an OPTIONAL... What does that mean?
         it will either hold the value requested, or it won't. This helps us avoid NullPointerExc.
         BECAUSE... we can't access the data if we don't use the .get() method
         Check out how it helps us write error handling functionality: */
        if (u.isEmpty()) {
            throw new IllegalArgumentException("No user found with username: " + newReservation.getUser().getUsername());
        } else {
            //set the user object in the new Reservation
            newReservation.setUser(u.get()); //.get() is what extracts the value from the Optional
            //send the Reservation to the DAO
            return rDAO.save(newReservation);
        }
    }

    @Transactional
    public Reservation resolveReservation(int id, String status) {
        if (!(status.equals("pending") || status.equals("approved") || status.equals("denied"))) {
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
        Optional<User> u = Optional.ofNullable(uDAO.findByUsername(username).getFirst());
        if (u.isEmpty()) {
            throw new IllegalArgumentException("No user found with username: " + username);
        } else {
            return rDAO.findByUser(u.get());
        }
    }
    public List<Reservation> getPendingUserReservations(String username) {
        Optional<User> u = Optional.ofNullable(uDAO.findByUsername(username).getFirst());
        if (u.isEmpty()) {
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
}