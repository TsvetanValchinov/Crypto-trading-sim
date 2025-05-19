package com.example.backend.Account;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.math.BigDecimal;

@Repository
public class AccountRepository {

    private final JdbcTemplate jdbcTemplate;
    private final AccountRowMapper accountRowMapper = new AccountRowMapper();

    public AccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public  Optional<Account> findByUserId(Long userId) {
        String sql = "SELECT * FROM accounts WHERE user_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, accountRowMapper, userId));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public  Optional<Account> findByAccountId(Long accountId) {
        String sql = "SELECT * FROM accounts WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, accountRowMapper, accountId));
        }
        catch (Exception e) {
            return Optional.empty();
        }
    }

    public  Account save(Account account) {
        if (account.getId() == null) {
            String sql = "INSERT INTO accounts (user_id, balance, created_at, updated_at)" +
                    "VALUES (?, ?, GETDATE(), GETDATE()); SELECT SCOPE_IDENTITY()";
            String identitySql = "SELECT SCOPE_IDENTITY()";
            try{
                Long id = jdbcTemplate.queryForObject(sql, Long.class, account.getUserId(), account.getBalance());
                account.setId(id);
                account.setInitialBalance(new BigDecimal(10000));
            }
            catch (org.springframework.dao.DuplicateKeyException e){
                Optional<Account> checkedAccount = findByUserId(account.getUserId());
                if(checkedAccount.isPresent()) {
                   return checkedAccount.get();
                }
            }
        }
        else {
            String sql = "UPDATE accounts SET balance = ?, updated_at = GETDATE() WHERE id = ?";
            jdbcTemplate.update(sql, account.getBalance(), account.getId());
        }
        return account;
    }

    public  void updateBalance(Long accountId, BigDecimal newBalance) {
        String sql = "UPDATE accounts SET balance = ?, updated_at = GETDATE() WHERE id = ?";
        jdbcTemplate.update(sql, newBalance, accountId);
    }

    public  void resetBalance(Long accountId) {
        String sql = "UPDATE accounts SET balance = initial_balance, updated_at = GETDATE() WHERE id = ?";
        jdbcTemplate.update(sql, accountId);
    }


}
