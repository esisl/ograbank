package com.example.bankcards.service;

import com.example.bankcards.dto.AuthRequestDto;
import com.example.bankcards.dto.AuthResponseDto;

public interface AuthService {
    AuthResponseDto register(AuthRequestDto dto);
    AuthResponseDto login(AuthRequestDto dto);
}