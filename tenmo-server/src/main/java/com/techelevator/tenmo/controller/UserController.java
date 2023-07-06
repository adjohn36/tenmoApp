package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping(path="/users")
public class UserController {

    private UserDao userDao;

    public UserController (UserDao userDao) {
        this.userDao = userDao;
    }
    
    @GetMapping(path="/{username}/balance")
    public BigDecimal getBalance(@PathVariable String username) {
            BigDecimal balance = userDao.getBalance(username);
       return balance;
    }

}
