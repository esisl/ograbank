package com.example.bankcards.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

public class TransferRequestDto {
    @NotNull(message = "ID карты-отправителя обязателен")
    private UUID fromCardId;

    @NotNull(message = "ID карты-получателя обязателен")
    private UUID toCardId;

    @NotNull(message = "Сумма перевода обязательна")
    @Positive(message = "Сумма должна быть больше нуля")
    private BigDecimal amount;

    public UUID getFromCardId() { return fromCardId; }
    public void setFromCardId(UUID fromCardId) { this.fromCardId = fromCardId; }
    public UUID getToCardId() { return toCardId; }
    public void setToCardId(UUID toCardId) { this.toCardId = toCardId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}