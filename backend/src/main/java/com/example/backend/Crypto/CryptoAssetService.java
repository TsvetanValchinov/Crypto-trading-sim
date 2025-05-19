package com.example.backend.Crypto;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CryptoAssetService {

    private final CryptoAssetRepository cryptoAssetRepository;

    public CryptoAssetService(CryptoAssetRepository cryptoAssetRepository) {
        this.cryptoAssetRepository = cryptoAssetRepository;
    }

    public List<CryptoAsset> getAccountAssets(Long accountId) {
        return cryptoAssetRepository.findByAccountId(accountId);
    }

    public Optional<CryptoAsset> getAsset(Long accountId, String symbol) {
        return cryptoAssetRepository.findByAccountIdAndSymbol(accountId, symbol);
    }

    public BigDecimal getQuantity(Long accountId, String symbol) {
        return  cryptoAssetRepository.findByAccountIdAndSymbol(accountId, symbol)
                .map(CryptoAsset::getQuantity).orElse(BigDecimal.ZERO);
    }

    public void addOrUpdateAsset(Long accountId, String symbol, BigDecimal quantity) {
        Optional<CryptoAsset> checkedAsset = cryptoAssetRepository.findByAccountIdAndSymbol(accountId, symbol);
        if(checkedAsset.isPresent()) {
            CryptoAsset asset = checkedAsset.get();
            asset.setQuantity(asset.getQuantity().add(quantity)); // adds to the quantity there is already in the asset
            cryptoAssetRepository.save(asset);
        }
        else {
            CryptoAsset newAsset = new CryptoAsset();
            newAsset.setAccountId(accountId);
            newAsset.setSymbol(symbol);
            newAsset.setQuantity(quantity);
            cryptoAssetRepository.save(newAsset);
        }
    }

    // sets the new quantity
    public void updateQuantity(Long accountId, String symbol, BigDecimal quantity){
        Optional<CryptoAsset> checkedAsset = cryptoAssetRepository.findByAccountIdAndSymbol(accountId, symbol);
        if(checkedAsset.isPresent()) {
            CryptoAsset asset = checkedAsset.get();
            asset.setQuantity(quantity);
            cryptoAssetRepository.save(asset);
        }
        else {
            throw new IllegalArgumentException("This account does not have this asset " + symbol);
        }
    }

    public  void deleteAllByAccountId(Long accountId){
        cryptoAssetRepository.deleteByAccountId(accountId);
    }

    public void deleteByAccountIdAndSymbol(Long accountId, String symbol) {
        cryptoAssetRepository.deleteByAccountIdAndSymbol(accountId, symbol);
    }
}