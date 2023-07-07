package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfer (account_from_id, account_to_id, amount, transfer_status) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount(), transfer.getTransferStatusId());
    }

    @Override
    public void updateTransferStatus(int transferId, String status) {
        String sql = "UPDATE transfer SET transfer_status = ? WHERE transfer_id = ?";
        jdbcTemplate.update(sql, status, transferId);
    }

    @Override
    public Transfer getTransferById(int transferId) {
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, transferId);
        if (rowSet.next()) {
            return mapRowToTransfer(rowSet);
        }
        return null;
    }
    // Renamed getTransfer to getTransfers so it matches the method name used in the interface and make it easier to understand.
    @Override
    public List<Transfer> getTransfersByUserId(int userId) {
        List<Transfer> transferListByUser = new ArrayList<>();
        String sql = "SELECT * FROM transfer WHERE account_from_id = ? OR account_to_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId, userId);

        while (rowSet.next()) {
            transferListByUser.add(mapRowToTransfer(rowSet));
        }
        return transferListByUser;
    }
    @Override
    public Transfer requestTransfer(Transfer transferRequest) {
        String sql = "INSERT INTO transfer (account_from_id, account_to_id, amount, transfer_status) VALUES (?, ?, ?, 'Pending') RETURNING transfer_id";
        int transferId = jdbcTemplate.queryForObject(sql, Integer.class, transferRequest.getAccountFrom(), transferRequest.getAccountTo(), transferRequest.getAmount());
        transferRequest.setTransferId(transferId);
        return transferRequest;
    }

    @Override
    public List<Transfer> getPendingTransfers(int userId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer WHERE (account_from_id = ? OR account_to_id = ?) AND transfer_status = 'Pending'";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while (rowSet.next()) {
            transfers.add(mapRowToTransfer(rowSet));
        }
        return transfers;
    }

    private Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setTransferStatusId(rowSet.getString("transfer_status"));
        transfer.setAccountFrom(rowSet.getInt("account_from_id"));
        transfer.setAccountTo(rowSet.getInt("account_to_id"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        return transfer;
    }
}
