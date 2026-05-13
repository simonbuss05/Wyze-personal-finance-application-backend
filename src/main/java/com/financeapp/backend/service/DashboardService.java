package com.financeapp.backend.service;


import com.financeapp.backend.dto.DashboardSummaryResponse;
import com.financeapp.backend.entity.FinanceUser;
import com.financeapp.backend.repository.PlaidAccountRepository;
import com.financeapp.backend.repository.PlaidTransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final PlaidTransactionRepository plaidTransactionRepository;
    private PlaidAccountRepository plaidAccountRepository;

    public DashboardService(PlaidAccountRepository plaidAccountRepository, PlaidTransactionRepository plaidTransactionRepository) {
        this.plaidAccountRepository = plaidAccountRepository;
        this.plaidTransactionRepository = plaidTransactionRepository;
    }

    public DashboardSummaryResponse getDashboardSummary(FinanceUser financeUser) {
        Double totalAssets = round(plaidAccountRepository.sumDepositoryBalances(financeUser));
        Double totalLiabilities =round(plaidAccountRepository.sumCreditBalances(financeUser));
        Double netWorth = round(totalAssets - totalLiabilities);
        Double monthlySpending = round(plaidTransactionRepository.sumMonthlySpending(financeUser));
        int connectedAccounts = plaidAccountRepository.findAllByUser(financeUser).size();
        DashboardSummaryResponse dashboardSummaryResponse = new DashboardSummaryResponse();
        dashboardSummaryResponse.setTotalAssets(totalAssets);
        dashboardSummaryResponse.setTotalLiabilities(totalLiabilities);
        dashboardSummaryResponse.setNetWorth(netWorth);
        dashboardSummaryResponse.setMonthlySpending(monthlySpending);
        dashboardSummaryResponse.setConnectedAccounts(connectedAccounts);
        return dashboardSummaryResponse;
    }

    private Double round(Double value) {
        if (value == null) return 0.0;
        return Math.round(value * 100.0) / 100.0;
    }
}
