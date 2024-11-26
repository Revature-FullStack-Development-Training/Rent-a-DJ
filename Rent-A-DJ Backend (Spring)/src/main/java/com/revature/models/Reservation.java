package com.revature.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component //This Class will be registered as a Spring Bean
@Entity //This Class will be created as a table in the DB (In other words, a DB ENTITY)
@Table(name = "reservations") //@Table lets us set properties like table name. THIS IS NOT WHAT MAKES IT A TABLE
public class Reservation {

    @Id //This makes the field the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //This makes our PK auto-increment (like serial)
    private int reservationId;

    @Column(nullable = false)
    private LocalDateTime creationTime = LocalDateTime.now(ZoneId.of("America/Los_Angeles"));

    @Column(nullable = false)
    private LocalDateTime startdatetime = LocalDateTime.parse("2013-12-18T14:30");

    @Column(nullable = false)
    private LocalDateTime enddatetime = LocalDateTime.parse("2013-12-18T14:30");

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "djId") //this links our FK to the PK in User (has to be the same amount!!!)
    private DJ dj;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId") //this links our FK to the PK in User (has to be the same amount!!!)
    private User user;

    public int getReservationId() {
        return reservationId;
    }
    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public LocalDateTime getStartdatetime() {
        return startdatetime;
    }
    public void setStartdatetime(LocalDateTime startdatetime) {
        this.startdatetime = startdatetime;
    }

    public LocalDateTime getEnddatetime() {
        return enddatetime;
    }
    public void setEnddatetime(LocalDateTime enddatetime) {
        this.enddatetime = enddatetime;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public DJ getDj() {
        return dj;
    }
    public void setDj(DJ dj) {
        this.dj = dj;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Reservation() {}

    public Reservation(String location, String startdatetime, String enddatetime, DJ dj, User user, String status){
        this.location = location;
        this.startdatetime = LocalDateTime.parse(startdatetime);
        this.enddatetime = LocalDateTime.parse(enddatetime);
        this.dj = dj;
        this.user = user;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId=" + reservationId +
                ", creationTime=" + creationTime +
                ", location='" + location + '\'' +
                ", startdatetime=" + startdatetime +
                ", enddatetime=" + enddatetime +
                ", dj=" + dj +
                ", user=" + user +
                '}';
    }
}