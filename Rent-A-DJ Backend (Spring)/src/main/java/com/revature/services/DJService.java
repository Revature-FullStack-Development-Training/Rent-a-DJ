package com.revature.services;

import com.revature.daos.DJDAO;
import com.revature.models.DJ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DJService {

    //DAO to call CRUD methods
    private DJDAO dDAO;

    //Wire the DAO with constructor injection
    @Autowired
    public DJService(DJDAO dDAO){ this.dDAO = dDAO; }


    //User Stories-----------------------------------

    //A method to change a DJ's payrate
    public DJ changeRate(int djId, Double newRate){

        //create a DJ object for error handling and return
        DJ dj = dDAO.findByDJId(djId);

        //check that the DJ isn't null
        if (dj == null){
            throw new IllegalArgumentException("DJ with DJ ID: " + djId + " was not found!");
        }
        //check that the rate isn't blank
        else if (dj.getRate() == 0 || dj.getPassword() == null){
            throw new IllegalArgumentException("Rate cannot be 0!");
        }
        else {
            dj.setRate(newRate);
        }
        //save and return the updated user
        return dDAO.save(dj);
    }

    //A method to see all DJs
    public List<DJ> getAllDJs(){

        //not a lot of error handling we need to do here
        return dDAO.findAll();

    }

    //A method to change a DJ's password
    public DJ changePassword(int djId, String newPassword){

        //create a DJ object for error handling and return
        DJ dj = dDAO.findByDJId(djId);

        //check that the DJ isn't null
        if (dj == null){
            throw new IllegalArgumentException("DJ with DJ ID: " + djId + " was not found!");
        }
        //check that the password isn't blank
        else if (dj.getPassword().isBlank() || dj.getPassword() == null){
            throw new IllegalArgumentException("Password cannot be blank!");
        }
        else {
            dj.setPassword(newPassword);
        }
        //save and return the updated user
        return dDAO.save(dj);
    }


    //A method for admins to delete a DJ (should also delete any related reservations)
    public DJ removeDJ(int djId){

        //User object for error handling and return
        DJ deletedDJ = dDAO.findByDJId(djId);

        //Check that the userId passed in is valid and a corresponding user exists
        if (djId <= 0){
            throw new IllegalArgumentException("DJ Id must be greater than 0!");
        }
        else if (dDAO.findByDJId(djId) == null){
            throw new IllegalArgumentException("DJ with DJ ID: " + djId + " was not found!");
        }
        //Delete the user from the DB and return the deleted user
        else {
            dDAO.delete(deletedDJ);
            return deletedDJ;
        }
    }

}
