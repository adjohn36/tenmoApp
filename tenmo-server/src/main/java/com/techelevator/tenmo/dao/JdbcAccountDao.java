package com.techelevator.tenmo.dao;
import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
@Override
    public BigDecimal getBalance(String username) {
        String sql = "SELECT balance FROM account JOIN tenmo_user ON account.user_id = tenmo_user.user_id WHERE tenmo_user.username = ?;";
        BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, username);
        return balance;
    }
@Override
    public void updateAccountBalance(int accountId, BigDecimal newBalance) {
        String sql = "UPDATE account SET balance = ? WHERE account_id = ?";
        jdbcTemplate.update(sql, newBalance, accountId);
    }

    @Override
    public Account getAccountByUserId(int userId) {
        String sql = "SELECT * FROM account WHERE user_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
        if (rowSet.next()) {
            return mapRowToAccount(rowSet);

        }else {
            return null;
        }
    }
    public Account getAccountByAccountId(int accountId) {
        String sql = "SELECT * FROM account WHERE account_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, accountId);
        if (rowSet.next()){
            return mapRowToAccount(rowSet);
        }else{
            return null;
        }
    }

    public int getAccountIdByUserId(int userId) {
        String sql = "SELECT account_id FROM account WHERE user_id = ?";
        int accountId = jdbcTemplate.queryForObject(sql, int.class, userId);
        return accountId;
    }

    @Override
    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT account_id, user_id FROM account";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql);
        while (rowSet.next()) {
            accounts.add(mapRowToAccount(rowSet));
        }
        return accounts;
    }


    private Account mapRowToAccount(SqlRowSet rowSet){
        Account account = new Account();
        account.setAccountId(rowSet.getInt("account_id"));
        account.setUserId(rowSet.getInt("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;
    }



}
