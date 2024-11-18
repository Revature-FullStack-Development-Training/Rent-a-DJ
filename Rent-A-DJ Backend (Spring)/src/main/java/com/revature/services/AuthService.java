package com.revature.services;

import com.revature.controllers.AuthController;
import com.revature.daos.AuthDAO;
import com.revature.models.DTOs.LoginDTO;
import com.revature.models.DTOs.OutgoingUserDTO;
import com.revature.models.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    //autowire the DAO

    private AuthDAO aDAO;

    @Autowired
    public AuthService(AuthDAO aDAO) {
        this.aDAO = aDAO;
    }

    public OutgoingUserDTO login(LoginDTO lDTO, HttpSession session){

        //Use the DTO data to find a user in the DB (through the DAO)
        User u = aDAO.findByUsernameAndPassword(lDTO.getUsername(), lDTO.getPassword());

        //If no user is found, throw an Exception
        if(u == null){
            //TODO: we could have made a custom LoginFailed Exception, but no time
            throw new IllegalArgumentException("No user found with those credentials!");
        }

        //If a user is found, login was successful! Initialize our session
        //Remember we're using the Session that lives on the Controller layer
        AuthController.session = session;

        //Store the User info in the session
        AuthController.session.setAttribute("userId", u.getUserId());
        AuthController.session.setAttribute("username", u.getUsername());
        AuthController.session.setAttribute("role", u.getRole());

        //Why store all this user info in session attributes?

        /* -It helps store user info that we can use to check if:
            -they're logged in
            -their role is appropriate for the functionality they want to access
            -personalize the app (use their name, etc)
            -simplify our URLs!
                ex: use the stored userId in "findBy" methods so we don't need to include it in the URL */

        //Process the User into an OutgoingUserDTO and return it!
        return new OutgoingUserDTO(u.getUserId(), u.getUsername(), u.getRole());

    }


}
