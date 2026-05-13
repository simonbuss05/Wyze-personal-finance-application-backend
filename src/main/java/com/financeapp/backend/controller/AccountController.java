package com.financeapp.backend.controller;

import com.financeapp.backend.dto.AccountResponse;
import com.financeapp.backend.entity.FinanceUser;
import com.financeapp.backend.service.AccountService;
import com.financeapp.backend.service.FinanceUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins="http://localhost:3000")
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private AccountService accountService;
    private FinanceUserService financeUserService;

    public AccountController(AccountService accountService, FinanceUserService financeUserService) {
        this.accountService = accountService;
        this.financeUserService = financeUserService;
    }

    @GetMapping
    public ResponseEntity<?> getAccounts() throws Exception {
        FinanceUser financeUser = getCurrentUser();
        List<AccountResponse> accounts = accountService.getAccountsForUser(financeUser);
        return ResponseEntity.ok(accounts);
    }


    private FinanceUser getCurrentUser() throws Exception{
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        FinanceUser financeUser = (FinanceUser) financeUserService.loadUserByUsername(email);

        return financeUser;
    }

}
