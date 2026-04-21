package com.verifime.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record InvoiceLine (
        String description,
        @NotNull(message = "Currency must not be null")
        String currency,
        @NotNull(message = "Amount must not be null")
        @Positive(message = "Amount must be positive")
        BigDecimal amount
){}
