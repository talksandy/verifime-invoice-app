package com.verifime.controller;

import com.verifime.dto.InvoiceRequest;
import com.verifime.service.InvoiceService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import org.jboss.logging.Logger;

@Path("/invoice")
public class InvoiceResource {

    @Inject
    InvoiceService invoiceService;

    private static final Logger LOG = Logger.getLogger(InvoiceResource.class);

    @POST
    @Path("/total")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response calculateTotal(@Valid InvoiceRequest request) {
        LOG.infof("InvoiceResource:calculateTotal - request=%s", request);
        BigDecimal total = invoiceService.calculateTotal(request);
        LOG.infof("InvoiceResource:calculateTotal Invoice total calculated successfully: %s", total);

        return Response.ok(total.toString()).build();
    }
}
