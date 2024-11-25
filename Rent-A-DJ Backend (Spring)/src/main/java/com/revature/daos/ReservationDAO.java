package com.revature.daos;

import com.revature.models.Reservation;
import com.revature.models.User;
import com.revature.models.DJ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository //make this class a Bean (1 of the 4 steretype annotations)
public interface ReservationDAO extends JpaRepository<Reservation, Integer> {
    Reservation findByReservationId(int reservationId);

    List<Reservation> findByUser(User user);
    List<Reservation> findByStatus(String status);
    List<Reservation> findByUserAndStatus(User user, String status);
    List<Reservation> findBydjIdAndUsername(int djId, String username);
}