package com.verifime.dto;

import com.verifime.validator.ValidCurrency;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PastOrPresent;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(name = "Invoice", description = "Invoice input including base currency, date, and line items")
public record Invoice (
    @Schema(example = "USD", description = "Three-letter ISO base currency code")
    @NotBlank(message = "Currency must not be empty/null")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Invalid currency code")
    @ValidCurrency
    String currency,
    @Schema(example = "2023-10-01", description = "Invoice date in ISO-8601 format")
    @NotNull(message = "Date must not be empty/null")
    @PastOrPresent(message = "Date cannot be in the future")
    LocalDate date,
    @Schema(required = true, description = "Line items to include in the total")
    @NotEmpty(message = "Invoice lines must not be empty")
     List<@Valid InvoiceLine> lines
) {}
