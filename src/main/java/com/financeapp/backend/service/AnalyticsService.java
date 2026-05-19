package com.financeapp.backend.service;

import com.financeapp.backend.entity.FinanceUser;
import com.financeapp.backend.entity.PlaidAccount;
import com.financeapp.backend.repository.PlaidAccountRepository;
import com.financeapp.backend.repository.PlaidTransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class AnalyticsService {

    private PlaidAccountRepository plaidAccountRepository;
    private PlaidTransactionRepository plaidTransactionRepository;

    public AnalyticsService(PlaidTransactionRepository plaidTransactionRepository,  PlaidAccountRepository plaidAccountRepository) {
        this.plaidTransactionRepository = plaidTransactionRepository;
        this.plaidAccountRepository = plaidAccountRepository;
    }

    public List<Map<String, Object>> getMonthlySpending(FinanceUser financeUser) {
        LocalDate startDate = LocalDate.now().minusMonths(5).withDayOfMonth(1);
        List<Object[]> results = plaidTransactionRepository.getMonthlySpending(financeUser.getId(), startDate);

        List<Map<String, Object>> monthlySpending = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> dataPoint = new HashMap<>();
            dataPoint.put("month", row[0].toString());
            dataPoint.put("total", Math.round(((Number) row[1]).doubleValue() * 100.0) / 100.0);
            monthlySpending.add(dataPoint);
        }
        return monthlySpending;
    }

    public List<Map<String, Object>> getCategoryBreakdown(FinanceUser financeUser) {
        List<Object[]> results = plaidTransactionRepository.getCategoryBreakdown(financeUser.getId());
        List<Map<String, Object>> categoryBreakdown = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> dataPoint = new HashMap<>();
            dataPoint.put("category", row[0].toString());
            dataPoint.put("total", Math.round(((Number) row[1]).doubleValue() * 100.0) / 100.0);
            categoryBreakdown.add(dataPoint);
        }

        return categoryBreakdown;
    }

    public List<String> getInsights(FinanceUser financeUser) {
        List<String> insights = new ArrayList<>();

        // Insight 1 — Biggest transaction this month
        try {
            Object[] biggest = plaidTransactionRepository.getBiggestTransactionThisMonth(financeUser.getId());
            if (biggest != null) {
                String name = biggest[0] != null ? biggest[0].toString() : biggest[1].toString();
                double amount = ((Number) biggest[2]).doubleValue();
                insights.add(String.format("Your biggest transaction this month was $%.2f at %s", amount, name));
            }
        } catch (Exception e) {
            System.out.println("Could not generate biggest transaction insight");
        }

        // Insight 2 — Month over month change
        try {
            LocalDate thisMonthStart = LocalDate.now().withDayOfMonth(1);
            LocalDate lastMonthStart = LocalDate.now().minusMonths(1).withDayOfMonth(1);
            LocalDate lastMonthEnd = thisMonthStart.minusDays(1);

            Double thisMonth = plaidTransactionRepository.sumSpendingBetweenDates(financeUser.getId(), thisMonthStart, LocalDate.now());
            Double lastMonth = plaidTransactionRepository.sumSpendingBetweenDates(financeUser.getId(), lastMonthStart, lastMonthEnd);

            if (thisMonth != null && lastMonth != null && lastMonth > 0) {
                double change = ((thisMonth - lastMonth) / lastMonth) * 100;
                if (change > 0) {
                    insights.add(String.format("You spent %.0f%% more this month than last month", change));
                } else {
                    insights.add(String.format("You spent %.0f%% less this month than last month", Math.abs(change)));
                }
            }
        } catch (Exception e) {
            System.out.println("Could not generate month over month insight");
        }

        // Insight 3 — Top category
        try {
            List<Object[]> categories = plaidTransactionRepository.getCategoryBreakdown(financeUser.getId());
            if (categories != null && !categories.isEmpty()) {
                String topCategory = categories.get(0)[0].toString()
                        .replace("_", " ")
                        .toLowerCase()
                        .substring(0, 1).toUpperCase() + categories.get(0)[0].toString()
                        .replace("_", " ")
                        .toLowerCase()
                        .substring(1);
                insights.add("Your top spending category this month is " + topCategory);
            }
        } catch (Exception e) {
            System.out.println("Could not generate top category insight");
        }

        // Insight 4 — Highest balance account
        try {
            List<PlaidAccount> accounts = plaidAccountRepository.findAccountsByBalanceDesc(financeUser);
            if (accounts != null && !accounts.isEmpty()) {
                PlaidAccount top = accounts.get(0);
                String name = top.getNickname() != null ? top.getNickname() : top.getName();
                insights.add(String.format("Your highest balance account is %s with $%.2f", name, top.getCurrentBalance()));
            }
        } catch (Exception e) {
            System.out.println("Could not generate highest balance insight");
        }

        return insights;
    }
}
