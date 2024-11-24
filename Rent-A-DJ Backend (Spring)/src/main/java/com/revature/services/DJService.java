package com.revature.services;

import com.revature.daos.DJDAO;
import com.revature.models.DJ;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DJService {

    //DAO to call CRUD methods
    private DJDAO dDAO;

    //Wire the DAO with constructor injection
    @Autowired
    public DJService(DJDAO dDAO){ this.dDAO = dDAO; }


    //User Stories-----------------------------------

    //This method finds a dj by their username
    public DJ findByUsername(String username){

        Optional<DJ> foundDJ = dDAO.findByUsername(username);

        if(foundDJ.isPresent()){

            //return
            return foundDJ.get();
        }
        else {
            throw new IllegalArgumentException("No DJ found with username: " + username);
        }

    }

    //This method inserts new DJs into the DB
    public DJ registerDJ(String firstName, String lastName, String username,
                         String password, double rate) {
        DJ newDJ = new DJ(0, firstName, lastName, username, password, rate);

        // Check if the username already exists in the database
        dDAO.findByUsername(username).ifPresent(dj -> {
            throw new IllegalArgumentException("Username already exists!"); // Lambda expression, basically says if optional from ifPresent() contains a DJ object, then that username is already taken and throws exception.
        });

        //Make sure the username is present in the new User
        if(newDJ.getUsername() == null || newDJ.getUsername().isBlank()) {
            //It will be the Controller's job to handle this
            throw new IllegalArgumentException("Username cannot be empty!");
        }

        if(newDJ.getPassword() == null || newDJ.getPassword().isBlank()){
            //It will be the Controller's job to handle this
            throw new IllegalArgumentException("Password cannot be empty!");
        }
        //.save() is the JPA method to insert data into the DB. We can also use this for updates
        //It also returns the saved object, so we can just return the method call. Convenient!
        return dDAO.save(newDJ);
    }

    //A method to change a DJ's payrate
    public DJ changeRate(int djId, Double newRate){

        //create a DJ object for error handling and return
        DJ dj = dDAO.findByDjId(djId);

        //check that the DJ isn't null
        if (dj == null){
            throw new IllegalArgumentException("DJ with DJ ID: " + djId + " was not found!");
        }
        //check that the rate isn't blank
        if (newRate == 0 ){
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
        DJ dj = dDAO.findByDjId(djId);

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
        DJ deletedDJ = dDAO.findByDjId(djId);

        //Check that the userId passed in is valid and a corresponding user exists
        if (djId <= 0){
            throw new IllegalArgumentException("DJ Id must be greater than 0!");
        }
        else if (dDAO.findByDjId(djId) == null){
            throw new IllegalArgumentException("DJ with DJ ID: " + djId + " was not found!");
        }
        //Delete the user from the DB and return the deleted user
        else {
            dDAO.delete(deletedDJ);
            return deletedDJ;
        }
    }

}
