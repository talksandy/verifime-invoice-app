package com.verifime.dto;

import com.verifime.validator.ValidCurrency;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;
import java.util.List;

public record Invoice (
    @NotBlank(message = "Currency must not be empty/null")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Invalid currency code")
    @ValidCurrency
    String currency,
    @NotNull(message = "Date must not be empty/null")
    @PastOrPresent(message = "Date cannot be in the future")
    LocalDate date,
    @NotEmpty(message = "Invoice lines must not be empty")
     List<@Valid InvoiceLine> lines
) {}
