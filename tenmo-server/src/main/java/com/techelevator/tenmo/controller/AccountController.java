package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.dao.AccountDao;

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

    public AccountController(AccountDao accountDao){
        this.accountDao = accountDao;
    }

    @GetMapping(path = "/balance")
    public BigDecimal getBalance(Principal principal){
        BigDecimal balance = accountDao.getBalance(principal.getName());
        return balance;
    }

   // @PutMapping(path = "/balance")

//    public BigDecimal updateAccountBalance(Principal principal, @RequestParam BigDecimal balance) {
//        String username = principal.getName();
//        BigDecimal balance
//        Account account = accountDao.getAccountByUserID(userId);
//        int accountId = account.getAccountId();
//        accountDao.updateAccountBalance(accountId, balance);
//        return accountDao.getBalance(accountId);
//    }

    @GetMapping
    public Account getAccountByUserId(int userId){
        Account account = accountDao.getAccountByUserId(userId);
        return account;
    }

    @GetMapping
    public List<Account> getAllAccounts(){
        return accountDao.getAllAccounts();
    }

}