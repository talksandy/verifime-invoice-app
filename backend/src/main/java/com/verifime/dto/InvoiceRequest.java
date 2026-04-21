package com.verifime.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class InvoiceRequest {
    @NotNull(message = "Invoice must not be null")
    @Valid
    private Invoice invoice;

    public Invoice getInvoice() {
        return invoice;
    }
}
