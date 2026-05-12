package com.financeapp.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="plaid_items")
public class PlaidItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private FinanceUser plaidUser;

    private String accessToken;

    private String itemId;

    private String institutionId;

    private String institutionName;

    private String cursor;

    private LocalDateTime lastSyncedAt;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public PlaidItem() {

    }

    public PlaidItem(FinanceUser plaidUser, String accessToken, String itemId, String institutionId, String institutionName, String cursor) {
        this.plaidUser = plaidUser;
        this.accessToken = accessToken;
        this.itemId = itemId;
        this.institutionId = institutionId;
        this.institutionName = institutionName;
        this.cursor = cursor;
        this.lastSyncedAt = LocalDateTime.now();
    }

    public FinanceUser getPlaidUser() {
        return plaidUser;
    }
    public void setPlaidUser(FinanceUser plaidUser) {
        this.plaidUser = plaidUser;
    }
    public String getAccessToken() {
        return accessToken;
    }
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    public String getItemId() {
        return itemId;
    }
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
    public String getInstitutionId() {
        return institutionId;
    }
    public void setInstitutionId(String institutionId) {
        this.institutionId = institutionId;
    }
    public String getInstitutionName() {
        return institutionName;
    }
    public PlaidItem setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
        return null;
    }
    public String getCursor() {
        return cursor;
    }
    public void setCursor(String cursor) {
        this.cursor = cursor;
    }
    public LocalDateTime getLastSyncedAt() {
        return lastSyncedAt;
    }
    public void setLastSyncedAt(LocalDateTime lastSyncedAt) {
        this.lastSyncedAt = lastSyncedAt;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

}
