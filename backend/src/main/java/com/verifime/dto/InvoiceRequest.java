package com.verifime.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record InvoiceRequest (
    @NotNull(message = "Invoice must not be null")
    @Valid Invoice invoice
) {}
