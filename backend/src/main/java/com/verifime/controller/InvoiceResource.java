package com.verifime.controller;

import com.verifime.dto.InvoiceRequest;
import com.verifime.service.InvoiceService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.math.BigDecimal;

@Path("/invoice")
public class InvoiceResource {

    @Inject
    InvoiceService invoiceService;

    @POST
    @Path("/total")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String calculateTotal(InvoiceRequest request) {
        BigDecimal total = invoiceService.calculateTotal(request);
        return total.toString();
    }
}
