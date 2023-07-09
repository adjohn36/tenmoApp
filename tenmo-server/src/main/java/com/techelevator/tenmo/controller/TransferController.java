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
//TODO: Need tested
    // 401 not authenticated
    // Send a transfer
    @RequestMapping(path = "/transfers/send", method = RequestMethod.POST)
    public Transfer sendTransfer(@RequestBody Transfer transfer, Principal principal, User accountTo) {
        int fromUserId = userDao.findIdByUsername(principal.getName());
        int fromUserAccountId = accountDao.getAccountIdByUserId(fromUserId);
        int toUserId = userDao.findIdByUsername(accountTo.getUsername());
        int toUserAccountId = accountDao.getAccountIdByUserId(toUserId);
        // Check if sender and receiver are the same user
        if (fromUserAccountId == toUserAccountId) {
            throw new IllegalArgumentException("Cannot send money to yourself.");
        }
        // Check if sender has sufficient balance
        BigDecimal senderBalance = accountDao.getBalance(principal.getName());
        BigDecimal transferAmount = transfer.getAmount();
        if (transferAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid transfer amount. Amount must be greater than zero.");
        }
        // TODO: test to see if compareTo method works.  If not try .subtract method.
        if (senderBalance.intValue() < 0) {
            throw new IllegalArgumentException("Insufficient balance. You cannot send more than your account balance.");
        }
        // Update sender's and receiver's account balances
        BigDecimal senderNewBalance = senderBalance.subtract(transferAmount);
        BigDecimal receiverNewBalance = accountDao.getBalance(accountTo.getUsername()).add(transferAmount);
        accountDao.updateAccountBalance(fromUserAccountId, senderNewBalance);
        accountDao.updateAccountBalance(toUserAccountId, receiverNewBalance);
        transfer.setAccountFrom(fromUserId);
        transfer.setTransferStatus("Approved");
        transferDao.sendTransfer(transfer);

        return transferDao.getTransferById(transfer.getTransferId());
    }

    // working
    @PostMapping(path = "/transfers/request")
    public Transfer requestTransfer(@RequestBody Transfer transfer, Principal principal) {
        int toUserId = userDao.findIdByUsername(principal.getName());
        int toUserAccountId = accountDao.getAccountIdByUserId(toUserId);
        int fromUserAccountID = accountDao.getAccountIdByAccountFromId(toUserAccountId);
        transfer.setAccountFrom(fromUserAccountID);
        transfer.setAccountTo(toUserAccountId);
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

