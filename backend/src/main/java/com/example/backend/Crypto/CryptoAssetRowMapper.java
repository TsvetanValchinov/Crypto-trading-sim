package com.example.backend.Crypto;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CryptoAssetRowMapper implements RowMapper<CryptoAsset> {
    @Override
    public CryptoAsset mapRow(ResultSet rs, int rowNum) throws SQLException {
        CryptoAsset asset = new CryptoAsset();
        asset.setId(rs.getLong("id"));
        asset.setAccountId(rs.getLong("account_id"));
        asset.setSymbol(rs.getString("symbol"));
        asset.setQuantity(rs.getBigDecimal("quantity"));
        asset.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        asset.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return asset;
    }
}