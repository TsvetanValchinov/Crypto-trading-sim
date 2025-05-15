package com.example.backend.repositories;

import com.example.backend.mappers.CryptoAssetRowMapper;
import com.example.backend.models.CryptoAsset;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;

@Repository
public class CryptoAssetRepository {

    private final JdbcTemplate jdbcTemplate;
    private  final CryptoAssetRowMapper cryptoAssetRowMapper = new CryptoAssetRowMapper();

    public CryptoAssetRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CryptoAsset> findByAccountId(Long accountId) {
        String sql = "SELECT * FROM cryptocurrency_assets  WHERE account_id = ?";
        return jdbcTemplate.query(sql, cryptoAssetRowMapper, accountId);
    }

    public  Optional<CryptoAsset> findByAccountIdAndSymbol(Long accountId, String symbol) {
        String sql = "SELECT * FROM cryptocurrency_assets WHERE account_id = ? AND symbol = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, cryptoAssetRowMapper, accountId, symbol));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public  void save(CryptoAsset asset){
        if(asset.getId() == null) {
            String sql = "INSERT INTO cryptocurrency_assets (account_id, symbol, quantity, created_at, updated_at) " +
                    "VALUES (?, ?, ?, GETDATE(), GETDATE()); SELECT SCOPE_IDENTITY();";
            Long id = jdbcTemplate.queryForObject(sql, Long.class,
                    asset.getAccountId(), asset.getSymbol(), asset.getQuantity());
            asset.setId(id);
        }
        else {
            String sql = "UPDATE cryptocurrency_assets SET quantity = ?, updated_at = GETDATE() WHERE id = ?";
            jdbcTemplate.update(sql, asset.getQuantity(), asset.getId());
        }
    }

    public  void deleteByAccountId(Long accountId) {
        String sql = "DELETE FROM cryptocurrency_assets WHERE id = ?";
        jdbcTemplate.update(sql, accountId);
    }
}
