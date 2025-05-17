package com.example.backend.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
public class RegistrationController {
    private final AccountService accountService;

    @Autowired
    public RegistrationController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<Account> createOrGetAccount(@PathVariable Long userId) {
        Account account = accountService.getOrCreateUser(userId);
        return ResponseEntity.ok(account);
    }
}
