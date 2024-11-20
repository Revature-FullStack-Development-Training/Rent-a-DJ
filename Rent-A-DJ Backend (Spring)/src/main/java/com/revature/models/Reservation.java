package com.revature.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component //This Class will be registered as a Spring Bean
@Entity //This Class will be created as a table in the DB (In other words, a DB ENTITY)
@Table(schema="project2", name = "reservations") //@Table lets us set properties like table name. THIS IS NOT WHAT MAKES IT A TABLE
public class Reservation {

    @Id //This makes the field the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //This makes our PK auto-increment (like serial)
    private int reservationId;

    @Column(nullable = false)
    private LocalDateTime creationTime = LocalDateTime.now(ZoneId.of("America/Los_Angeles"));

    @Column(nullable = false)
    private LocalDateTime starttimedate = LocalDateTime.parse("2013-12-18T14:30");

    @Column(nullable = false)
    private LocalDateTime endtimedate = LocalDateTime.parse("2013-12-18T14:30");

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String status;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "djId",referencedColumnName = "djId") //this links our FK to the PK in User (has to be the same amount!!!)
    private DJ dj;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId",referencedColumnName = "userId") //this links our FK to the PK in User (has to be the same amount!!!)
    private User user;

    public Reservation() {}

    public Reservation(String location, String starttimedate, String endtimedate, String status, DJ dj, User user){
        this.location = location;
        this.starttimedate = LocalDateTime.parse(starttimedate);
        this.endtimedate = LocalDateTime.parse(endtimedate);
        this.dj = dj;
        this.user = user;
        this.status = status;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public LocalDateTime getStarttimedate() {
        return starttimedate;
    }

    public void setStarttimedate(LocalDateTime starttimedate) {
        this.starttimedate = starttimedate;
    }

    public LocalDateTime getEndtimedate() {
        return endtimedate;
    }

    public void setEndtimedate(LocalDateTime endtimedate) {
        this.endtimedate = endtimedate;
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

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId=" + reservationId +
                ", creationTime=" + creationTime +
                ", location='" + location + '\'' +
                ", starttimedate=" + starttimedate +
                ", endtimedate=" + endtimedate +
                ", dj=" + dj +
                ", user=" + user +
                '}';
    }
}