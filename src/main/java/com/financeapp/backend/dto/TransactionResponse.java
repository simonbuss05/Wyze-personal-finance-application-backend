package com.financeapp.backend.dto;

import java.time.LocalDate;

public class TransactionResponse {

    private Long id;
    private String plaidTransactionId;
    private Double amount;
    private String merchantName;
    private String name;
    private String category;
    private LocalDate date;
    private boolean pending;
    private String accountName;
    private String accountMask;
    private String institutionName;

    public TransactionResponse() {

    }

    public TransactionResponse(Long id, String plaidTransactionId, Double amount, String merchantName, String name, String category, LocalDate date, boolean pending, String accountName, String accountMask, String institutionName) {
        this.id = id;
        this.plaidTransactionId = plaidTransactionId;
        this.amount = amount;
        this.merchantName = merchantName;
        this.name = name;
        this.category = category;
        this.date = date;
        this.pending = pending;
        this.accountName = accountName;
        this.accountMask = accountMask;
        this.institutionName = institutionName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlaidTransactionId() {
        return plaidTransactionId;
    }

    public void setPlaidTransactionId(String plaidTransactionId) {
        this.plaidTransactionId = plaidTransactionId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountMask() {
        return accountMask;
    }

    public void setAccountMask(String accountMask) {
        this.accountMask = accountMask;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }
    public String getInstitutionName() {
        return institutionName;
    }
}
