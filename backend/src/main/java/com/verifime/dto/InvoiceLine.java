package com.verifime.dto;

import com.verifime.validator.ValidCurrency;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record InvoiceLine (
        String description,
        @NotBlank(message = "Currency must not be empty/null")
        @Pattern(regexp = "^[A-Z]{3}$", message = "Invalid currency code")
        @ValidCurrency
        String currency,
        @NotNull(message = "Amount must not be null")
        @Positive(message = "Amount must be positive")
        BigDecimal amount
){}
