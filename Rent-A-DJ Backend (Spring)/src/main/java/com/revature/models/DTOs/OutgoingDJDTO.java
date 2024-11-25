package com.revature.models.DTOs;

public class OutgoingDJDTO {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private double rate;

    //no-args constructor
    public OutgoingDJDTO() {
    }

    //all-args constructor
    public OutgoingDJDTO(String firstName, String lastName, String username, String password, double rate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.rate = rate;
    }

    //first name getter
    public String getFirstName() {
        return firstName;
    }

    //first name setter
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    //last name getter
    public String getLastName() {
        return lastName;
    }

    //last name setter
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    //username getter
    public String getUsername() {
        return username;
    }

    //username setter
    public void setUsername(String username) {
        this.username = username;
    }

    //password getter
    public String getPassword() {
        return password;
    }

    //password setter
    public void setPassword(String password) {
        this.password = password;
    }

    //rate getter
    public double getRate() {
        return rate;
    }

    //rate setter
    public void setRate(double rate) {
        this.rate = rate;
    }
}
