package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
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

    public TransferController(TransferDao transferDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    // Get all transfers for the current user
    @RequestMapping(path = "/transfers", method = RequestMethod.GET)
    public List<Transfer> getAllTransfers(Principal principal) {
        User user = userDao.findByUsername(principal.getName());
        int userId = user.getId().intValue();
        return transferDao.getTransfersByUserId(userId);
    }

    // Get a specific transfer by its ID
    @RequestMapping(path = "/transfers/{transferId}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int transferId) {
        return transferDao.getTransferById(transferId);
    }

    // Send a transfer
    @RequestMapping(path = "/transfers/send", method = RequestMethod.POST)
    public Transfer sendTransfer(@RequestBody Transfer transfer, Principal principal, User accountTo) {
        User fromUser = userDao.findByUsername(principal.getName());
        User toUser = userDao.findByUsername(accountTo.getUsername());
        int fromUserId = fromUser.getId().intValue();
        // Check if sender and receiver are the same user
        if (fromUser.getId().equals(toUser.getId())) {
            throw new IllegalArgumentException("Cannot send money to yourself.");
        }
        // Check if sender has sufficient balance
//        BigDecimal senderBalance = userDao.getBalance(fromUser.getUsername());
//        BigDecimal transferAmount = transfer.getAmount();
//        if (transferAmount.compareTo(BigDecimal.ZERO) <= 0) {
//            throw new IllegalArgumentException("Invalid transfer amount. Amount must be greater than zero.");
//        }
//        // TODO: test to see if compareTo method works.  If not try .subtract method.
//        if (senderBalance.compareTo(transferAmount) < 0) {
//            throw new IllegalArgumentException("Insufficient balance. You cannot send more than your account balance.");
//        }
//        // Update sender's and receiver's account balances
//        BigDecimal senderNewBalance = senderBalance.subtract(transferAmount);
//        BigDecimal receiverNewBalance = userDao.getBalance(toUser.getUsername()).add(transferAmount);
//        userDao.updateAccountBalance(fromUser.getId().intValue(), senderNewBalance);
//        userDao.updateAccountBalance(toUser.getId().intValue(), receiverNewBalance);
//        transfer.setAccountFrom(fromUserId);
//        transfer.setTransferStatusId("Approved");
//        transferDao.createTransfer(transfer);

        return transfer;
    }

    // Request a transfer
    @RequestMapping(path = "/transfers/request", method = RequestMethod.POST)
    public Transfer requestTransfer(@RequestBody Transfer transfer, Principal principal) {
        User toUser = userDao.findByUsername(principal.getName());
        int toUserId = toUser.getId().intValue();
        transfer.setAccountTo(toUserId);
        transfer.setTransferStatusId("Pending");
        return transferDao.requestTransfer(transfer);
    }

    // Get all pending transfers for the current user
    @RequestMapping(path = "/transfers/pending", method = RequestMethod.GET)
    public List<Transfer> getPendingTransfers(Principal principal) {
        User user = userDao.findByUsername(principal.getName());
        int userId = user.getId().intValue();
        return transferDao.getPendingTransfers(userId);
    }

    // Approve a pending transfer
    @RequestMapping(path = "/transfers/approve/{transferId}", method = RequestMethod.PUT)
    public void approveTransfer(@PathVariable int transferId) {
        transferDao.updateTransferStatus(transferId, "Approved");
    }

    // Reject a pending transfer
    @RequestMapping(path = "/transfers/reject/{transferId}", method = RequestMethod.PUT)
    public void rejectTransfer(@PathVariable int transferId) {
        transferDao.updateTransferStatus(transferId, "Rejected");
    }
}
