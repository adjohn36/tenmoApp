package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(path="/users")
public class UserController {

    private UserDao userDao;

    public UserController (UserDao userDao) {
        this.userDao = userDao;
    }

    
}
