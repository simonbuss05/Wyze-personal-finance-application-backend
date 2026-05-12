package com.financeapp.backend.dto;

public class ExchangeTokenRequest {
    private String publicToken;

    public ExchangeTokenRequest() {

    }
    public ExchangeTokenRequest(String publicToken) {
        this.publicToken = publicToken;
    }
    public String getPublicToken() {
        return publicToken;
    }
    public void setPublicToken(String publicToken) {
        this.publicToken = publicToken;
    }

}
