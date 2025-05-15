package com.example.backend.repositories;

import com.example.backend.mappers.TransactionRowMapper;
import com.example.backend.models.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class TransactionRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TransactionRowMapper transactionRowMapper = new TransactionRowMapper();

    public TransactionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Transaction> findByAccountId(Long accountId) {
        String sql = "SELECT * FROM transactions WHERE account_id = ? ORDER BY timestamp DESC";
        return jdbcTemplate.query(sql, transactionRowMapper, accountId);
    }

    public  Transaction save(Transaction transaction) {
        String sql = "INSERT INTO transactions (account_id, symbol, quantity, price, type, timestamp, profit_loss)" +
                "VALUES (?, ?, ?, ?, ?, GETDATE(), ?); SELECT id, timestamp FROM transactions WHERE id = SCOPE_IDENTITY()";
        Object[] result = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Object[]{rs.getLong("id"), rs.getTimestamp("timestamp").toLocalDateTime()},
                transaction.getAccountId(),
                transaction.getSymbol(),
                transaction.getQuantity(),
                transaction.getPrice(),
                transaction.getType().toString(),
                transaction.getProfitLoss());
        Long id = (Long) result[0];
        return  new Transaction(id,
                transaction.getAccountId(),
                transaction.getSymbol(),
                transaction.getQuantity(),
                transaction.getPrice(),
                transaction.getType(),
                (java.time.LocalDateTime) result[1],
                transaction.getProfitLoss());
    }

    public  void deleteByAccountId(Long accountId) {
        String sql = "DELETE FROM transactions WHERE account_id = ?";
        jdbcTemplate.update(sql, accountId);
    }



}
