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



