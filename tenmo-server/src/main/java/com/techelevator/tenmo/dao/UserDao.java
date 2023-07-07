package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.Transfer;


import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public interface UserDao {

    List<User> findAll();

    User findByUsername(String username);

    int findIdByUsername(String username);

    boolean create(String username, String password);

    BigDecimal getBalance(String username);

    //New methods i just put in
    List<Transfer> getPendingTransfers(int userId);

    void updateAccountBalance(int accountId, BigDecimal newBalance);

    void updateTransferStatus(int transferId, String status);

}