package com.revature.daos;

import com.revature.models.DJ;
import com.revature.models.Reservation;
import com.revature.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository //make this class a Bean (1 of the 4 stereotype annotations)
public interface ReservationDAO extends JpaRepository<Reservation, Integer> {
    Reservation findByReservationId(int reservationId);

    List<Reservation> findByUser(User user);
    List<Reservation> findByUserAndStatus(User user, String status);
    List<Reservation> findByDj_DjIdAndDj_Username(int djId, String username);
    List<Reservation> findByDj_DjIdAndStatus(DJ dj, String status);

    @Query("SELECT r FROM Reservation r WHERE LOWER(r.status) = LOWER(:status)")
    List<Reservation> findByStatus(@Param("status") String status);
}