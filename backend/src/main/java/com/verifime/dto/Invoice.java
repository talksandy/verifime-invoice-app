package com.verifime.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record Invoice (
    @NotNull(message = "Currency must not be null")
     String currency,
    @NotNull(message = "Date must not be null")
     LocalDate date,
    @NotEmpty(message = "Invoice lines must not be empty")
    @Valid
     List<InvoiceLine> lines
) {}
