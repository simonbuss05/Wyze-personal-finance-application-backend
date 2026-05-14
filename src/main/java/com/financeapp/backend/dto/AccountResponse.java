package com.financeapp.backend.dto;

public class AccountResponse {
    private Long id;
    private String plaidAccountId;
    private String name;
    private String officialName;
    private String type;
    private String subtype;
    private String mask;
    private Double currentBalance;
    private Double availableBalance;
    private String currencyCode;
    private String institutionName;
    private String nickname;

    public AccountResponse(Long id, String plaidAccountId, String name, String officialName, String type, String subtype, String mask, Double currentBalance, Double availableBalance, String currencyCode, String institutionName, String nickname) {
        this.id = id;
        this.plaidAccountId = plaidAccountId;
        this.name = name;
        this.officialName = officialName;
        this.type = type;
        this.subtype = subtype;
        this.mask = mask;
        this.currentBalance = currentBalance;
        this.availableBalance = availableBalance;
        this.currencyCode = currencyCode;
        this.institutionName = institutionName;
        this.nickname = nickname;
    }

    public AccountResponse() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
