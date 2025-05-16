package com.example.backend.Transaction;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionRowMapper implements RowMapper<Transaction> {
    @Override
    public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Transaction(
                rs.getLong("id"),
                rs.getLong("account_id"),
                rs.getString("symbol"),
                rs.getBigDecimal("quantity"),
                rs.getBigDecimal("price"),
                TransactionType.valueOf(rs.getString("type")),
                rs.getTimestamp("timestamp").toLocalDateTime(),
                rs.getBigDecimal("profit_loss")
        );
    }
}
