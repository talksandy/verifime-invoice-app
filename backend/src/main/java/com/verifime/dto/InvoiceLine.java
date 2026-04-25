package com.verifime.dto;

import com.verifime.validator.ValidCurrency;
import jakarta.validation.constraints.*;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "InvoiceLine", description = "Individual invoice line item")
public record InvoiceLine (
        @Schema(example = "Consulting services", description = "Optional line-item description")
        String description,
        @Schema(example = "EUR", description = "Three-letter ISO currency code for the line amount")
        @NotBlank(message = "Currency must not be empty/null")
        @Pattern(regexp = "^[A-Z]{3}$", message = "Invalid currency code")
        @ValidCurrency
        String currency,
        @Schema(example = "50.00", description = "Positive line-item amount")
        @NotNull(message = "Amount must not be null")
        @Positive(message = "Amount must be positive")
        BigDecimal amount
){}
