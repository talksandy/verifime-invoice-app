package com.verifime.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name = "InvoiceRequest", description = "Top-level invoice calculation request payload")
public record InvoiceRequest (
    @Schema(required = true, description = "Invoice details to validate and total")
    @NotNull(message = "Invoice must not be null")
    @Valid Invoice invoice
) {}
