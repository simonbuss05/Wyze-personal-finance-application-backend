package com.financeapp.backend.dto;

import java.time.LocalDateTime;

public class PlaidItemResponse {
    private Long id;
    private String institutionName;
    private String institutionId;
    private int accountCount;
    private LocalDateTime createdAt;

    public PlaidItemResponse(Long id, String institutionName, String institutionId, int accountCount, LocalDateTime createdAt) {
        this.id = id;
        this.institutionName = institutionName;
        this.institutionId = institutionId;
        this.accountCount = accountCount;
        this.createdAt = LocalDateTime.now();
    }

    public PlaidItemResponse() {

    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(String institutionId) {
        this.institutionId = institutionId;
    }

    public int getAccountCount() {
        return accountCount;
    }

    public void setAccountCount(int accountCount) {
        this.accountCount = accountCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
