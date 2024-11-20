package com.revature.daos;

import com.revature.models.Reservation;
import com.revature.models.User;
import com.revature.models.DJ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

//Check UserDAO for general notes about how Spring Data DAOs work

@Repository //make this class a Bean (1 of the 4 steretype annotations)
public interface ReservationDAO extends JpaRepository<Reservation, Integer> {

}