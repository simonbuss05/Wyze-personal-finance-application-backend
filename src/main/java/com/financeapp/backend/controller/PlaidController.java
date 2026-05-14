package com.financeapp.backend.controller;

import com.financeapp.backend.dto.ExchangeTokenRequest;
import com.financeapp.backend.entity.FinanceUser;
import com.financeapp.backend.entity.PlaidItem;
import com.financeapp.backend.repository.PlaidItemRepository;
import com.financeapp.backend.service.FinanceUserService;
import com.financeapp.backend.service.PlaidService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/plaid")
public class PlaidController {

    private PlaidService plaidService;

    private FinanceUserService financeUserService;

    private PlaidItemRepository plaidItemRepository;

    public PlaidController(PlaidService plaidService, FinanceUserService financeUserService,  PlaidItemRepository plaidItemRepository) {
        this.plaidService = plaidService;
        this.financeUserService = financeUserService;
        this.plaidItemRepository = plaidItemRepository;
    }

    private FinanceUser getCurrentUser() throws Exception{
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        FinanceUser financeUser = (FinanceUser) financeUserService.loadUserByUsername(email);

        return financeUser;
    }

    @PostMapping("/create-link-token")
    public ResponseEntity<?> createLinkToken() throws Exception {
        FinanceUser financeUser = getCurrentUser();
        String linkToken = plaidService.createLinkToken(financeUser);
        return ResponseEntity.ok(Map.of("link_token", linkToken));
    }

    @PostMapping("/exchange-token")
    public ResponseEntity<?> exchangeToken(@RequestBody ExchangeTokenRequest exchangeTokenRequest) throws IOException {
        try {
            FinanceUser financeUser = getCurrentUser();
            PlaidItem plaidItem = plaidService.exchangeToken(exchangeTokenRequest.getPublicToken(), financeUser);
            plaidService.syncAccounts(plaidItem);
            plaidService.syncTransactions(plaidItem);
            return ResponseEntity.ok(Map.of("status", "success"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("items/{itemId}")
    public ResponseEntity<?> deleteItem(@PathVariable Long itemId) throws Exception {
        try {
            FinanceUser financeUser = getCurrentUser();
            plaidService.disconnectPlaidItem(itemId, financeUser);
            return ResponseEntity.ok(Map.of("status", "success"));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }


}
