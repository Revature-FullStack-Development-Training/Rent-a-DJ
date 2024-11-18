package com.revature.aspects;

import com.revature.controllers.AuthController;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthAspect {

    @Before("execution(* com.revature.controllers.UserController.*(..)) " +
            "&& !execution(* com.revature.controllers.UserController.registerUser(..))")
    public void checkLogin() {

        if (AuthController.session == null) {
            throw new IllegalArgumentException("You must be logged in to do this!");
        }

    }

    @Before("@annotation(com.revature.aspects.AdminOnly)")
    public void checkAdmin() {
        if (!AuthController.session.getAttribute("role").equals("admin")) {
            throw new IllegalArgumentException("You must be an admin to do this!");
        }
    }
}