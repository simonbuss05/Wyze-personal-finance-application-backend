package com.financeapp.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="plaid_transactions")
public class PlaidTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name="account_id")
    private PlaidAccount plaidAccount;

    @ManyToOne
    @JoinColumn(name="user_id")
    private FinanceUser plaidUser;

    private String plaidTransactionId;

    private Double amount;

    private String merchantName;

    private String name;

    private String category;

    private LocalDate date;

    private boolean pending;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public PlaidTransaction(PlaidAccount plaidAccount, FinanceUser plaidUser, String plaidTransactionId, Double amount, String merchantName, String name, String category, boolean pending, LocalDate date) {
        this.plaidAccount = plaidAccount;
        this.plaidUser = plaidUser;
        this.plaidTransactionId = plaidTransactionId;
        this.amount = amount;
        this.merchantName = merchantName;
        this.name = name;
        this.category = category;
        this.date = date;
        this.pending = pending;
    }

    public PlaidTransaction() {

    }

    public PlaidAccount getPlaidAccount(PlaidAccount byPlaidAccountId) {
        return plaidAccount;
    }

    public void setPlaidAccount(PlaidAccount plaidAccount) {
        this.plaidAccount = plaidAccount;
    }

    public FinanceUser getPlaidUser() {
        return plaidUser;
    }

    public void setPlaidUser(FinanceUser plaidUser) {
        this.plaidUser = plaidUser;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
