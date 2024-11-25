package com.revature.models.DTOs;

import com.revature.models.User;
import jakarta.persistence.Column;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class OutgoingReservationDTO {

    //fields based on addReservation in ReservationService
    private String location;
    private String startdatetime;
    private String enddatetime;
    private int djId;
    private int userId;
    private String status;

    //no-args constructor
    public OutgoingReservationDTO() {
    }

    //all args constructor
    public OutgoingReservationDTO(String location, String startdatetime, String enddatetime, int djId, int userId, String status) {
        this.location = location;
        this.startdatetime = startdatetime;
        this.enddatetime = enddatetime;
        this.djId = djId;
        this.userId = userId;
        this.status = status;
    }

    //location getter
    public String getLocation() {
        return location;
    }

    //location setter
    public void setLocation(String location) {
        this.location = location;
    }

    //start time getter
    public String getStartdatetime() {
        return startdatetime;
    }

    //start time setter
    public void setStartdatetime(String startdatetime) {
        this.startdatetime = startdatetime;
    }

    //end time getter
    public String getEnddatetime() {
        return enddatetime;
    }

    //end time setter
    public void setEnddatetime(String enddatetime) {
        this.enddatetime = enddatetime;
    }

    //DJ id getter
    public int getDjId() {
        return djId;
    }

    //DJ id setter
    public void setDjId(int djId) {
        this.djId = djId;
    }

    //User id getter
    public int getUserId() {
        return userId;
    }

    //User id setter
    public void setUserId(int userId) {
        this.userId = userId;
    }

    //status getter
    public String getStatus() {
        return status;
    }

    //status setter
    public void setStatus(String status) {
        this.status = status;
    }

    //toString
    @Override
    public String toString() {
        return "OutgoingReservationDTO{" +
                "location='" + location + '\'' +
                ", startdatetime='" + startdatetime + '\'' +
                ", enddatetime='" + enddatetime + '\'' +
                ", djId=" + djId +
                ", userId=" + userId +
                ", status='" + status + '\'' +
                '}';
    }
}
