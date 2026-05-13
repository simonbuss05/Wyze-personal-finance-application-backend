package com.financeapp.backend.dto;

public class DashboardSummaryResponse {
    private Double netWorth;
    private Double totalAssets;
    private Double totalLiabilities;
    private Double monthlySpending;
    private int connectedAccounts;

    public DashboardSummaryResponse(Double netWorth, Double totalAssets, Double totalLiabilities, Double monthlySpending, int connectedAccounts) {
        this.netWorth = netWorth;
        this.totalAssets = totalAssets;
        this.totalLiabilities = totalLiabilities;
        this.monthlySpending = monthlySpending;
        this.connectedAccounts = connectedAccounts;
    }

    public DashboardSummaryResponse() {

    }

    public Double getNetWorth() {
        return netWorth;
    }

    public void setNetWorth(Double netWorth) {
        this.netWorth = netWorth;
    }

    public Double getTotalAssets() {
        return totalAssets;
    }

    public void setTotalAssets(Double totalAssets) {
        this.totalAssets = totalAssets;
    }

    public Double getTotalLiabilities() {
        return totalLiabilities;
    }

    public void setTotalLiabilities(Double totalLiabilities) {
        this.totalLiabilities = totalLiabilities;
    }

    public Double getMonthlySpending() {
        return monthlySpending;
    }

    public void setMonthlySpending(Double monthlySpending) {
        this.monthlySpending = monthlySpending;
    }

    public int getConnectedAccounts() {
        return connectedAccounts;
    }

    public void setConnectedAccounts(int connectedAccounts) {
        this.connectedAccounts = connectedAccounts;
    }
}
