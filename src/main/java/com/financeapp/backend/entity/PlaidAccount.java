package com.financeapp.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="plaid_accounts")
public class PlaidAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name="plaid_item_id")
    private PlaidItem plaidItem;

    @ManyToOne
    @JoinColumn(name="user_id")
    private FinanceUser user;

    private String plaidAccountId;

    private String name;

    private String officialName;

    private String type;

    private String subtype;

    private String mask;

    private Double currentBalance;

    private Double availableBalance;

    private String currencyCode;

    private LocalDateTime updatedAt;

    public PlaidAccount(PlaidItem plaidItem, FinanceUser user, String plaidAccountId, String name, String officialName, String type, String subtype, String mask, Double currentBalance, Double availableBalance, String currencyCode) {
        this.plaidItem = plaidItem;
        this.user = user;
        this.plaidAccountId = plaidAccountId;
        this.name = name;
        this.officialName = officialName;
        this.type = type;
        this.subtype = subtype;
        this.mask = mask;
        this.currentBalance = currentBalance;
        this.availableBalance = availableBalance;
        this.currencyCode = currencyCode;
        this.updatedAt = LocalDateTime.now();
    }
    public PlaidAccount() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlaidItem getPlaidItem() {
        return plaidItem;
    }

    public void setPlaidItem(PlaidItem plaidItem) {
        this.plaidItem = plaidItem;
    }

    public FinanceUser getUser() {
        return user;
    }

    public void setUser(FinanceUser user) {
        this.user = user;
    }

    public String getPlaidAccountId() {
        return plaidAccountId;
    }

    public void setPlaidAccountId(String plaidAccountId) {
        this.plaidAccountId = plaidAccountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfficialName() {
        return officialName;
    }

    public void setOfficialName(String officialName) {
        this.officialName = officialName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public Double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Double currentBalance) {
        this.currentBalance = currentBalance;
    }

    public Double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(Double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
