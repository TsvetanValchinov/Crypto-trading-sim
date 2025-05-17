package com.example.backend.Account;

import com.example.backend.Crypto.CryptoAsset;
import com.example.backend.Crypto.CryptoAssetService;
import com.example.backend.Transaction.TradeRequestDTO;
import com.example.backend.Transaction.Transaction;
import com.example.backend.Transaction.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;
    private final CryptoAssetService cryptoAssetService;

    public AccountController(AccountService accountService,
                             TransactionService transactionService,
                             CryptoAssetService cryptoAssetService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.cryptoAssetService = cryptoAssetService;
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Long accountId) {
        return ResponseEntity.ok(accountService.getBalance(accountId));
    }

    @GetMapping("/{accountId}/assets")
    public ResponseEntity<List<CryptoAsset>> getAssets(@PathVariable Long accountId) {
        return ResponseEntity.ok(cryptoAssetService.getAccountAssets(accountId));
    }

    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable Long accountId) {
        return ResponseEntity.ok(transactionService.getTransactionHistory(accountId));
    }

    @PostMapping("/{accountId}/buy")
    public ResponseEntity<?> buyCrypto(@PathVariable Long accountId, @RequestBody TradeRequestDTO request) {
        try{
            Transaction result = transactionService.buyCrypto(accountId, request.getSymbol(), BigDecimal.valueOf(request.getQuantity()));
            return ResponseEntity.ok(result);
        }
        catch (IllegalArgumentException | InsufficientResourcesException e){
            return ResponseEntity.badRequest().body("Buy failed: " + e.getMessage());
        }
    }

    @PostMapping("{accountId}/sell")
    public ResponseEntity<?> sellCrypto(@PathVariable Long accountId, @RequestBody TradeRequestDTO request){
        try{
            Transaction result = transactionService.sellCrypto(accountId, request.getSymbol(), BigDecimal.valueOf(request.getQuantity()));
            return ResponseEntity.ok(result);
        }
        catch (IllegalArgumentException | InsufficientResourcesException e){
            return ResponseEntity.badRequest().body("Sell failed: " + e.getMessage());
        }
    }

    @PostMapping("/accountId/reset")
    public ResponseEntity<String> resetAccount(@PathVariable Long accountId) {
        accountService.resetAccount(accountId);
        return ResponseEntity.ok("Account reset");
    }
}
