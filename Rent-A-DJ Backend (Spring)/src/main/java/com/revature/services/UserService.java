package com.revature.services;

import com.revature.daos.UserDAO;
import com.revature.models.DJ;
import com.revature.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/*What is the Service layer?? It's also known as the "Business Logic Layer" because...

    -This is where we do any data processing/error handling that DOESN'T have to do with the DB or HTTP
        -DAO handles DB operations
        -Controller handles HTTP requests/responses
    -EVERYTHING in between should live in the Service layer! */

@Service //1 of the 4 stereotype annotations. It registers this Class as a Spring Bean
public class UserService {

    //We can't instantiate Interfaces like Classes... how do we get access to our DAO methods?
    //DEPENDENCY INJECTION! With the @Autowired dependency
    private UserDAO uDAO;

    //This is CONSTRUCTOR INJECTION (not setter injection, not field injection)
    @Autowired
    public UserService(UserDAO userDAO) {
        this.uDAO = userDAO;
    }

    /* TODO: pass this a user instead of individual fields (we can send in a user from the
       TODO: controller instead of the users fields) */

    //This method inserts new Users into the DB
    public User registerUser(String firstName, String lastName, String username, String password, String role){
        User newUser = new User(0, firstName, lastName, username, password, role);

        //Make sure the username is present in the new User
        if(newUser.getUsername() == null || newUser.getUsername().isBlank()) {
            //It will be the Controller's job to handle this
            throw new IllegalArgumentException("Username cannot be empty!");
        }
        if(newUser.getPassword() == null || newUser.getPassword().isBlank()){
            //It will be the Controller's job to handle this
            throw new IllegalArgumentException("Password cannot be empty!");
        }
        //User u = findByUsername(newUser.getUsername());
        //If u is not null, throw an exception because the username already exists
        if(!uDAO.findByUsername(newUser.getUsername()).isEmpty()){
            //It will be the Controller's job to handle this
            throw new IllegalArgumentException("Username already exists!");
        }
        //.save() is the JPA method to insert data into the DB. We can also use this for updates
        //It also returns the saved object, so we can just return the method call. Convenient!
        return uDAO.save(newUser);
    }

    public User deleteUser(int userid) {
        if (userid < 0){
            throw new IllegalArgumentException("ID must be greater than 0.");
        }

        //Find the user ID - if it exists, delete it, otherwise IllegalArgException
        User userToDelete = uDAO.findById(userid).orElseThrow(() ->
                new IllegalArgumentException("No user found with id: " + userid));

        uDAO.deleteById(userid); //Inherited from JpaRepository

        return userToDelete;
    }

    //A method to change a user's password
    public User changePassword(int userId, String newPassword){

        //create a User object for error handling and return
        User user = uDAO.findByUserId(userId);

        //check that the DJ isn't null
        if (user == null){
            throw new IllegalArgumentException("User with User ID: " + userId + " was not found!");
        }
        //check that the password isn't blank
        else if (user.getPassword().isBlank() || user.getPassword() == null){
            throw new IllegalArgumentException("Password cannot be blank!");
        }
        else {
            user.setPassword(newPassword);
        }
        //save and return the updated user
        return uDAO.save(user);
    }

    //A method to change a user's username
    public User changeUsername(int userId, String newUsername){

        //create a User object for error handling and return
        User user = uDAO.findByUserId(userId);

        //check that the DJ isn't null
        if (user == null){
            throw new IllegalArgumentException("User with User ID: " + userId + " was not found!");
        }
        //check that the username isn't blank
        else if (user.getUsername().isBlank() || user.getUsername() == null){
            throw new IllegalArgumentException("Password cannot be blank!");
        }
        else {
            user.setUsername(newUsername);
        }
        //save and return the updated user
        return uDAO.save(user);
    }

    //This method gets a user by username
    public User getUserByUsername(String username){

        //a little error handling
        if(username == null || username.isBlank()){
            throw new IllegalArgumentException("Please search for a valid username!");
        }

        // Assuming findByUsername returns a List<User>
        List<User> users = uDAO.findByUsername(username);

        // If no users are found, throw an exception
        if (users.isEmpty()) {
            throw new IllegalArgumentException("No user found with username: " + username);
        }
        // Return the first (and only) user from the list
        return users.getFirst();
    }

    public List<User> getUserByUsernameStartingWith(String username) {

        // Handle null or blank input gracefully
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Please search for a valid username: ");
        }

        // Find users whose usernames start with the given string
        List<User> users = uDAO.findByUsernameStartingWith(username);

        // If no users are found, throw an exception
        if (users == null || users.isEmpty()) {
            throw new IllegalArgumentException("No user found starting with: " + username);
        }

        return users;
    }
    //This method gets all users from the DB
    public List<User> getAllUsers(){
        //findAll() is a JPA method that returns all records in a table
        return uDAO.findAll();

        //Not much error handling in a get all... maybe checking to see if it's empty?
    }
}
