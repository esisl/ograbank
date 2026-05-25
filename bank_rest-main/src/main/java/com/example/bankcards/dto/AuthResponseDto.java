package com.example.bankcards.dto;

public class AuthResponseDto {
    private final String accessToken;
    public AuthResponseDto(String accessToken) { this.accessToken = accessToken; }
    public String getAccessToken() { return accessToken; }
}