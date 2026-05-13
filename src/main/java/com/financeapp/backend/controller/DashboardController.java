package com.financeapp.backend.controller;


import com.financeapp.backend.dto.DashboardSummaryResponse;
import com.financeapp.backend.entity.FinanceUser;
import com.financeapp.backend.service.DashboardService;
import com.financeapp.backend.service.FinanceUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private DashboardService dashboardService;
    private FinanceUserService financeUserService;

    public DashboardController(DashboardService dashboardService, FinanceUserService financeUserService) {
        this.dashboardService = dashboardService;
        this.financeUserService = financeUserService;
    }

    @GetMapping("/summary")
    public ResponseEntity<?> getDashboardSummary() throws Exception {
        FinanceUser financeUser = getCurrentUser();
        DashboardSummaryResponse summary = dashboardService.getDashboardSummary(financeUser);
        return ResponseEntity.ok(summary);
    }

    private FinanceUser getCurrentUser() throws Exception{
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        FinanceUser financeUser = (FinanceUser) financeUserService.loadUserByUsername(email);

        return financeUser;
    }

    //dashboard shows all info on frontend :)
}
