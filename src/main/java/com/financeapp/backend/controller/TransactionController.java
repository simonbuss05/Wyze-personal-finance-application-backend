package com.financeapp.backend.controller;

import com.financeapp.backend.dto.TransactionResponse;
import com.financeapp.backend.entity.FinanceUser;
import com.financeapp.backend.service.FinanceUserService;
import com.financeapp.backend.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private TransactionService transactionService;
    private FinanceUserService financeUserService;

    public TransactionController(TransactionService transactionService, FinanceUserService financeUserService) {
        this.transactionService = transactionService;
        this.financeUserService = financeUserService;
    }

    @GetMapping
    public ResponseEntity<?> getTransactions(@RequestParam(required = false) Long accountId,
                                             @RequestParam(required = false) String category,
                                             @RequestParam(required = false) String search,
                                             @RequestParam(required = false) String from,
                                             @RequestParam(required = false) String to,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "20") int size) throws Exception {

        LocalDate dateFrom = from != null ? LocalDate.parse(from) : null;
        LocalDate dateTo = to != null ? LocalDate.parse(to) : null;
        FinanceUser financeUser = getCurrentUser();

        Page<TransactionResponse> transactions = transactionService.getTransactionsForUser(
                financeUser, accountId, category, search, dateFrom, dateTo, page, size);

        Map<String, Object> response = new HashMap<>();
        response.put("transactions", transactions.getContent());
        response.put("totalElements", transactions.getTotalElements());
        response.put("totalPages", transactions.getTotalPages());
        response.put("currentPage", transactions.getNumber());
        response.put("hasMore", transactions.hasNext());

        return ResponseEntity.ok(response);
    }


    private FinanceUser getCurrentUser() throws Exception{
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        FinanceUser financeUser = (FinanceUser) financeUserService.loadUserByUsername(email);

        return financeUser;
    }

    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() throws Exception {
        FinanceUser user = getCurrentUser();
        List<String> categories = transactionService.getCategoriesForUser(user);
        return ResponseEntity.ok(categories);
    }
}
