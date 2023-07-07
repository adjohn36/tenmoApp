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
//    void updateTransferStatus(int transferId, String status);


//    @Override
//    public BigDecimal getBalance(String username) {
//        String sql = "SELECT balance FROM account JOIN tenmo_user ON account.user_id = tenmo_user.user_id WHERE tenmo_user.username = ?;";
//        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, username);
//        return balance;
//    }
//
//    // Retrieve pending transfers associated with a specific user from the transfer table
//    @Override
//    public List<Transfer> getPendingTransfers(int userId) {
//        List<Transfer> transfers = new ArrayList<>();
//        String sql = "SELECT * FROM transfer WHERE (account_from_id = ? OR account_to_id = ?) AND transfer_status = 'Pending'";
//        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId, userId);
//        while (rowSet.next()) {
//            transfers.add(mapRowToTransfer(rowSet));
//        }
//        return transfers;
//    }
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
//    public void updateTransferStatus(int transferId, String status) {
//        String sql = "UPDATE transfer SET transfer_status = ? WHERE transfer_id = ?";
//        jdbcTemplate.update(sql, status, transferId);
//    }
//private Transfer mapRowToTransfer(SqlRowSet rowSet) {
//    Transfer transfer = new Transfer();
//    transfer.setTransferId(rowSet.getInt("transfer_id"));
//    transfer.setTransferStatusId(rowSet.getString("transfer_status"));
//    transfer.setAccountFrom(rowSet.getInt("account_from_id"));
//    transfer.setAccountTo(rowSet.getInt("account_to_id"));
//    transfer.setAmount(rowSet.getBigDecimal("amount"));
//    return transfer;
//}