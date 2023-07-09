package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

public interface AccountDao {

    BigDecimal getBalance(String username);

    void updateAccountBalance(int accountId, BigDecimal newBalance);

    Account getAccountByUserId(int userId);

    int getAccountIdByUserId(int userId);

    List<Account> getAllAccounts();
}
