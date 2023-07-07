package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public interface TransferDao {

    // Create a transfer in the transfer table
    void createTransfer(Transfer transfer);

    // Update the status of a transfer in the transfer table
    void updateTransferStatus(int transferId, String status);

    // Retrieve a transfer by its ID from the transfer table
    Transfer getTransferById(int transferId);

    // Retrieve transfers associated with a specific user from the transfer table
    List<Transfer> getTransfersByUserId(int userId);

    // Retrieve pending transfers associated with a specific user from the transfer table
    List<Transfer> getPendingTransfers(int userId);

    // Create a transfer request in the transfer table
    Transfer requestTransfer(Transfer transferRequest);



}

//TODO: create accountDao and paste different methods into accountDao and transferDao
//    BigDecimal getBalance(String username);
//
//    //New methods i just put in
//    List<Transfer> getPendingTransfers(int userId);
//
//    void updateAccountBalance(int accountId, BigDecimal newBalance);
//
//    @Override
//    public BigDecimal getBalance(String username) {
//        String sql = "SELECT balance FROM account JOIN tenmo_user ON account.user_id = tenmo_user.user_id WHERE tenmo_user.username = ?;";
//        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, username);
//        return balance;
//    }
//
//
//
//    // Update the account balance in the account table
//    @Override
//    public void updateAccountBalance(int accountId, BigDecimal newBalance) {
//        String sql = "UPDATE account SET balance = ? WHERE account_id = ?";
//        jdbcTemplate.update(sql, newBalance, accountId);
//    }
//
//    // Update the transfer status in the transfer table
//    @Override

