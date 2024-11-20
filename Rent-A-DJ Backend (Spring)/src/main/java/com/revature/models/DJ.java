package com.revature.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component //This Class will be registered as a Spring Bean
@Entity //This Class will be created as a table in the DB (In other words, a DB ENTITY)
@Table(schema="project2", name = "DJs") //@Table lets us set properties like table name. THIS IS NOT WHAT MAKES IT A TABLE
public class DJ {

    @Id //This makes the field the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) //This makes our PK auto-increment (like serial)
    private int DJId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "DJ", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnore //prevents the circular reference in our JSON responses
    private List<Reservation> reservations;

    public DJ() {}

    public DJ(int userid, String firstName, String lastName, String username, String password, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}