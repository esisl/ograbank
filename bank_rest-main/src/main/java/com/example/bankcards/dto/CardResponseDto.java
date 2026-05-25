package com.example.bankcards.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class CardResponseDto {
    private final UUID id;
    private final String maskedNumber;
    private final String ownerName;
    private final LocalDate expiryDate;
    private final String status;
    private final BigDecimal balance;
    private final LocalDateTime createdAt;

    public CardResponseDto(UUID id, String maskedNumber, String ownerName,
                           LocalDate expiryDate, String status, BigDecimal balance,
                           LocalDateTime createdAt) {
        this.id = id;
        this.maskedNumber = maskedNumber;
        this.ownerName = ownerName;
        this.expiryDate = expiryDate;
        this.status = status;
        this.balance = balance;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public String getMaskedNumber() { return maskedNumber; }
    public String getOwnerName() { return ownerName; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public String getStatus() { return status; }
    public BigDecimal getBalance() { return balance; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}