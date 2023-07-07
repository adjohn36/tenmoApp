package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    void createTransfer (Transfer transfer);

    void updateTransferStatus(int transferId, String status);

    Transfer getTransferById(int transferId);

    List<Transfer> getTransfersByUserId(int userId);

    List<Transfer> getPendingTransfers(int userId);

    Transfer requestTransfer (Transfer transferRequest);
}
