package com.financeapp.backend.dto;

public class NicknameRequest {

    private String nickname;

    public NicknameRequest(String nickname) {
        this.nickname = nickname;
    }
    public NicknameRequest() {

    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
