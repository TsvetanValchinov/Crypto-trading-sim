package com.example.backend.Account;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.backend.Crypto.CryptoAssetRepository;
import com.example.backend.Transaction.TransactionRepository;
import javax.naming.InsufficientResourcesException;
import java.math.BigDecimal;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final CryptoAssetRepository cryptoAssetRepository;
    private final TransactionRepository transactionRepository;

    private static final BigDecimal INITIAL_BALANCE = new BigDecimal("10000.00");

    public AccountService(AccountRepository accountRepository, CryptoAssetRepository cryptoAssetRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.cryptoAssetRepository = cryptoAssetRepository;
        this.transactionRepository = transactionRepository;
    }

    public  Account getOrCreateUser(Long userId)
    {
        return accountRepository.findByUserId(userId).orElseGet(() -> {
            Account newAccount = new Account();
            newAccount.setUserId(userId);
            newAccount.setBalance(INITIAL_BALANCE);
            newAccount.setInitialBalance(INITIAL_BALANCE);
            return accountRepository.save(newAccount);
        });
    }

    public BigDecimal getBalance(Long accountId) {
        return accountRepository.findByAccountId(accountId).map(Account::getBalance).orElse(BigDecimal.ZERO);
    }
    // TODO: should the finding be with byAccountId or byUserId?
    @Transactional
    public  void updateBalance(Long accountId, BigDecimal amount) throws InsufficientResourcesException {
        Account account = accountRepository.findByAccountId(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        BigDecimal newBalance = account.getBalance().add(amount);
        if(newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientResourcesException("Insufficient balance for this operation");
        }
        accountRepository.updateBalance(accountId, newBalance);
    }

    @Transactional
    public  void resetAccount(Long accountId) {
        cryptoAssetRepository.deleteByAccountId(accountId);
        transactionRepository.deleteByAccountId(accountId);
        accountRepository.resetBalance(accountId);
    }
}
