package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
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
    //this method gets the users balance, it responds to a GET request at "/{username}/balance
    //the username in the path is used to find the balance
    // TODO: getBalance hasnt been resolved yet. ERROR
    //@GetMapping(path="/{username}/balance")
    //public BigDecimal getBalance(@PathVariable String username) {
    //   return userDao.getBalance(username);
    //}

}
