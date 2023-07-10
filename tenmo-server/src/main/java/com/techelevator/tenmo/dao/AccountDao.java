package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

public interface AccountDao {

    BigDecimal getBalance(String username);

    void updateAccountBalance(int accountId, BigDecimal newBalance);

    BigDecimal addToAccountBalance(BigDecimal addedAmount, int accountId);

    BigDecimal subtractFromAccountBalance(BigDecimal subtractedAmount, int accountId);

    Account getAccountByUserId(int userId);

    Account getAccountByAccountId(int AccountId);

    int getAccountIdByUserId(int userId);

    List<Account> getAllAccounts();

    int getAccountIdByAccountFromId(int accountFromId);


}
