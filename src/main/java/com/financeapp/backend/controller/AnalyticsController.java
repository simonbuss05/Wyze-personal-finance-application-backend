package com.financeapp.backend.controller;


import com.financeapp.backend.entity.FinanceUser;
import com.financeapp.backend.service.AnalyticsService;
import com.financeapp.backend.service.FinanceUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private AnalyticsService analyticsService;
    private FinanceUserService financeUserService;

    public AnalyticsController(AnalyticsService analyticsService, FinanceUserService financeUserService) {
        this.analyticsService = analyticsService;
        this.financeUserService = financeUserService;
    }

    @GetMapping("/monthly-spending")
    public ResponseEntity<?> monthlySpending(@RequestParam(defaultValue = "12") int months) throws Exception {
        FinanceUser financeUser = getCurrentUser();
        List<Map<String, Object>> result = analyticsService.getMonthlySpending(financeUser, months);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/category-breakdown")
    public ResponseEntity<?> categorySpending() throws Exception {
        FinanceUser financeUser = getCurrentUser();
        List<Map<String, Object>> result = analyticsService.getCategoryBreakdown(financeUser);
        return ResponseEntity.ok(result);
    }


    @GetMapping("/insights")
    public ResponseEntity<?> insights() throws Exception {
        FinanceUser financeUser = getCurrentUser();
        List<String> insights = analyticsService.getInsights(financeUser);
        return ResponseEntity.ok(insights);
    }



    private FinanceUser getCurrentUser() throws Exception{
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        FinanceUser financeUser = (FinanceUser) financeUserService.loadUserByUsername(email);
        return financeUser;
    }
}
