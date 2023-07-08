package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(path = "/account")
public class AccountController {

    private AccountDao accountDao;
    private UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    // Get the account balance for the currently authenticated user
    @GetMapping(path = "/balance")
    public BigDecimal getBalance(Principal principal) {
        BigDecimal balance = accountDao.getBalance(principal.getName());
        return balance;
    }

    // Update the account balance for the currently authenticated user
    @PutMapping(path = "/balance")
    public void updateAccountBalance(Principal principal, @RequestParam BigDecimal balance) {
        String username = principal.getName();
        User user = userDao.findByUsername(username);
        int userId = user.getId().intValue();
        Account account = accountDao.getAccountByUserId(userId);
        int accountId = account.getAccountId();
        accountDao.updateAccountBalance(accountId, balance);
    }

    // Get the account by user ID
    @GetMapping(path = "/{userId}")
    public Account getAccountByUserId(@PathVariable("userId") int userId) {
        Account account = accountDao.getAccountByUserId(userId);
        return account;
    }

    // Get all accounts
    @GetMapping(path = "/all")
    public List<Account> getAllAccounts() {
        return accountDao.getAllAccounts();
    }
}
