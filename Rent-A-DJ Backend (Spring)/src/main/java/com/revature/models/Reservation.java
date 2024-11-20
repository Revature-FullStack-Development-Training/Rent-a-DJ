package com.revature.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Setter
@Getter
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
    private String location;

    @Column(nullable = false)
    private LocalDateTime timedate = LocalDateTime.parse("2013-12-18T14:30");

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "DJId",referencedColumnName = "DJId") //this links our FK to the PK in User (has to be the same amount!!!)
    private DJ dj;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId",referencedColumnName = "userId") //this links our FK to the PK in User (has to be the same amount!!!)
    private User user;

    public Reservation() {}

    public Reservation(String location, String timedate, DJ dj, User user){
        this.location = location;
        this.timedate = LocalDateTime.parse(timedate);
        this.dj = dj;
        this.user = user;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}