package com.example.bankcards.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;

public class CardCreateDto {
    @NotNull(message = "ID владельца обязателен")
    private UUID userId;

    @NotBlank(message = "Номер карты не может быть пустым")
    @Size(min = 16, max = 19, message = "Номер карты должен содержать 16-19 цифр")
    private String cardNumber;

    @NotBlank(message = "Имя владельца обязательно")
    private String ownerName;

    @NotNull(message = "Срок действия обязателен")
    private LocalDate expiryDate;

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
}