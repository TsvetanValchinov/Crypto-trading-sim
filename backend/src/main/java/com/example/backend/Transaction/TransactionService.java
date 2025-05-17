package com.example.backend.Transaction;

import com.example.backend.Account.AccountService;
import com.example.backend.Crypto.CryptoAsset;
import com.example.backend.Crypto.CryptoAssetRepository;
import com.example.backend.Crypto.KrakenWebSocketService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static java.math.BigDecimal.ROUND_HALF_UP;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CryptoAssetRepository cryptoAssetRepository;
    private final AccountService accountService;
    private final KrakenWebSocketService krakenWebSocketService;

    public TransactionService(TransactionRepository transactionRepository, CryptoAssetRepository cryptoAssetRepository,
                              AccountService accountService, KrakenWebSocketService krakenWebSocketService){
        this.transactionRepository = transactionRepository;
        this.cryptoAssetRepository = cryptoAssetRepository;
        this.accountService = accountService;
        this.krakenWebSocketService = krakenWebSocketService;
    }

    public List<Transaction> getTransactionHistory(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    @Transactional
    public Transaction buyCrypto(Long accountId, String symbol, BigDecimal quantity) throws InsufficientResourcesException {
        BigDecimal currPrice = krakenWebSocketService.getBuyPrice(symbol);
        if(currPrice.equals(BigDecimal.ZERO)) {
            throw new IllegalArgumentException("Price information not available for" + symbol);
        }
        BigDecimal totalCost = currPrice.multiply(quantity);
        accountService.updateBalance(accountId, totalCost.negate());

        Optional<CryptoAsset> existingAsset = cryptoAssetRepository.findByAccountIdAndSymbol(accountId, symbol);
        if(existingAsset.isPresent()){
            CryptoAsset asset = existingAsset.get();
            asset.setQuantity(asset.getQuantity().add(quantity));
            cryptoAssetRepository.save(asset);
        }
        else {
            CryptoAsset newAsset = new CryptoAsset();
            newAsset.setAccountId(accountId);
            newAsset.setSymbol(symbol);
            newAsset.setQuantity(quantity);
            cryptoAssetRepository.save(newAsset);
        }
        Transaction transaction = new Transaction(
                accountId,
                symbol,
                quantity,
                currPrice,
                TransactionType.fromString("BUY"),
                java.time.LocalDateTime.now(),
                null
        );
        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction sellCrypto(Long accountId, String symbol, BigDecimal quantity) throws InsufficientResourcesException {
        CryptoAsset asset = cryptoAssetRepository.findByAccountIdAndSymbol(accountId, symbol).orElseThrow(() -> new IllegalArgumentException("This account does not have this asset " + symbol));
        if(asset.getQuantity().compareTo(quantity) < 0) {
            throw new IllegalArgumentException("Insufficient quantity for this asset:" + symbol);
        }
        BigDecimal currPrice = krakenWebSocketService.getSellPrice(symbol);
        if(currPrice.equals(BigDecimal.ZERO))
            throw new IllegalArgumentException("Price information not available for" + symbol);

        BigDecimal totalSellAmount = currPrice.multiply(quantity);
        accountService.updateBalance(accountId, totalSellAmount);

        BigDecimal newQuantity = asset.getQuantity().subtract(quantity);
        if(newQuantity.compareTo(BigDecimal.ZERO) > 0) {
            asset.setQuantity(newQuantity);
            cryptoAssetRepository.save(asset);
        }
        else {
            cryptoAssetRepository.deleteByAccountId(accountId);
        }

        BigDecimal profitLoss = calculateProfitLoss(accountId, symbol, quantity, currPrice);

        Transaction transaction = new Transaction(
                accountId,
                symbol,
                quantity,
                currPrice,
                TransactionType.fromString("SELL"),
                java.time.LocalDateTime.now(),
                profitLoss
        );
        return transactionRepository.save(transaction);
    }

    private BigDecimal calculateProfitLoss(Long accountId, String symbol, BigDecimal quantity, BigDecimal currPrice) {
            BigDecimal totalBoughtQuantity = BigDecimal.ZERO;
            BigDecimal totalBoughtPrice = BigDecimal.ZERO;
            List<Transaction> prevTransactions = transactionRepository.findByAccountIdAndSymbol(accountId, symbol);
            for(Transaction t : prevTransactions)
            {
                if(t.getType() == TransactionType.BUY) {
                    totalBoughtQuantity = totalBoughtQuantity.add(t.getQuantity());
                    totalBoughtPrice = totalBoughtPrice.add(t.getPrice().multiply(t.getQuantity()));
                }
                if(totalBoughtQuantity.compareTo(quantity) >= 0 ) // breaks if we have the needed quantity
                    break;
            }
            if(totalBoughtQuantity.compareTo(quantity) < 0) {
                return BigDecimal.ZERO;
            }
            BigDecimal averagePrice = totalBoughtPrice.divide(totalBoughtQuantity);
            return currPrice.subtract(averagePrice).multiply(quantity);

    }
}
