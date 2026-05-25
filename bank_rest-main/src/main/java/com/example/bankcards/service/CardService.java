package com.example.bankcards.service;

import com.example.bankcards.dto.CardCreateDto;
import com.example.bankcards.dto.CardResponseDto;
import com.example.bankcards.dto.TransferRequestDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface CardService {
    CardResponseDto createCard(CardCreateDto dto);
    Page<CardResponseDto> getCardsByUserId(UUID userId, Pageable pageable);
    void transfer(TransferRequestDto dto);
}