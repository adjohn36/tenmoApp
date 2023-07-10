package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.data.relational.core.sql.In;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private TransferDao transferDao;
    private UserDao userDao;
    private AccountDao accountDao;

    public TransferController(TransferDao transferDao, UserDao userDao, AccountDao accountDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
        this.accountDao = accountDao;
    }

    // 200 -- works
    // Get all transfers for the current user
    @RequestMapping(path = "/transfers", method = RequestMethod.GET)
    public List<Transfer> getAllTransfers(Principal principal) {
        int userId = userDao.findIdByUsername(principal.getName());
        int accountId = accountDao.getAccountIdByUserId(userId);
        return transferDao.getTransfersByAccountId(accountId);
    }
    // 200 -- works
    // Get a specific transfer by its ID
    @RequestMapping(path = "/transfers/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int transferId) {
        return transferDao.getTransferById(transferId);
    }

    //TODO: Test it out
    @PostMapping(path = "/transfers/send")
    public Transfer sendTransfer(@RequestBody Transfer transfer, Principal principal) {
        int fromUserId = transfer.getAccountFrom();   // Retrieve the user ID of the sender
        int toUserId = transfer.getAccountTo();       // Retrieve the user ID of the receiver
        // Check if sender and receiver are the same user
        if (fromUserId == toUserId) {
            throw new IllegalArgumentException("Cannot send money to yourself.");
        }
        // Check if transfer amount is valid
        BigDecimal transferAmount = transfer.getAmount();
        if (transferAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid transfer amount. Amount must be greater than zero.");
        }
        // Retrieve the sender and receiver accounts
        Account senderAccount = accountDao.getAccountByUserId(fromUserId);
        Account receiverAccount = accountDao.getAccountByUserId(toUserId);

        // Check if sender has sufficient balance
        BigDecimal senderBalance = senderAccount.getBalance();
        if (senderBalance.compareTo(transferAmount) < 0) {
            throw new IllegalArgumentException("Insufficient balance. You cannot send more than your account balance.");
        }
        // Update sender's and receiver's account balances
        BigDecimal senderNewBalance = senderBalance.subtract(transferAmount);
        BigDecimal receiverNewBalance = receiverAccount.getBalance().add(transferAmount);

        // Update the sender's account balance and update the receiver's account balance
        accountDao.updateAccountBalance(senderAccount.getAccountId(), senderNewBalance);
        accountDao.updateAccountBalance(receiverAccount.getAccountId(), receiverNewBalance);

        // Set the transfer status to "Approved"
        transfer.setTransferStatus("Approved");

        // Perform the send transfer operation
        Transfer sentTransfer = transferDao.sendTransfer(transfer);

        // Add the transfer to the transfer lists of both the sender and receiver users
        userDao.addTransferForUser(fromUserId, sentTransfer);
        userDao.addTransferForUser(toUserId, sentTransfer);

        return sentTransfer;
    }

    //todo: working now.
    @PostMapping(path = "/transfers/request")
    public Transfer requestTransfer(@RequestBody Transfer transfer, Principal principal) {
        int fromAccountId = transfer.getAccountFrom();   // Retrieve the account ID of the user making the request
        int toAccountId = transfer.getAccountTo();       // Retrieve the account ID of the user receiving the request

        // Check if sender and receiver are the same account
        if (fromAccountId == toAccountId) {
            throw new IllegalArgumentException("Cannot request money to yourself.");
        }

        // Check if transfer amount is valid
        BigDecimal transferAmount = transfer.getAmount();
        if (transferAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid transfer amount. Amount must be greater than zero.");
        }

        // Perform the request transfer operation and return the transfer object
        transfer.setTransferStatus("Pending");
        return transferDao.requestTransfer(transfer);
    }

    // 200 -- works, returns pending transfer for accountId
    // Get all pending transfers for the current user
    @RequestMapping(path = "/transfers/pending", method = RequestMethod.GET)
    public List<Transfer> getPendingTransfers(Principal principal) {
        int userId = userDao.findIdByUsername(principal.getName());
        int accountId = accountDao.getAccountIdByUserId(userId);
        return transferDao.getPendingTransfers(accountId);
    }
    // 200 -- works
    // Approve a pending transfer
    @RequestMapping(path = "/transfers/approve/{transferId}", method = RequestMethod.PUT)
    public void approveTransfer(@PathVariable int transferId) {
        transferDao.updateTransferStatus(transferId, "Approved");
    }
    // 200 -- works
    // Reject a pending transfer
    @RequestMapping(path = "/transfers/reject/{transferId}", method = RequestMethod.PUT)
    public void rejectTransfer(@PathVariable int transferId) {
        transferDao.updateTransferStatus(transferId, "Rejected");
    }


}

