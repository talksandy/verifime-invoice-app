package com.verifime.controller;

import com.verifime.dto.InvoiceRequest;
import com.verifime.service.InvoiceService;
import io.micrometer.core.annotation.Timed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.ExampleObject;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.logging.Logger;

@Tag(name = "Invoice", description = "Operations for invoice validation and total calculation")
@Path("/invoice")
public class InvoiceResource {

    @Inject
    InvoiceService invoiceService;

    private static final Logger LOG = Logger.getLogger(InvoiceResource.class);

    @POST
    @Path("/total")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Timed(value = "invoice.calculate.total", description = "Time spent calculating invoice totals")
    @Operation(
            summary = "Calculate invoice total",
            description = "Validates the invoice payload, fetches exchange rates when needed, and returns the total in the invoice base currency."
    )
    @RequestBody(
            required = true,
            description = "Invoice payload to validate and calculate",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = InvoiceRequest.class),
                    examples = @ExampleObject(
                            name = "invoice-request",
                            value = """
                                    {
                                      "invoice": {
                                        "currency": "USD",
                                        "date": "2023-10-01",
                                        "lines": [
                                          { "description": "Item1", "currency": "USD", "amount": 100.00 },
                                          { "description": "Item2", "currency": "EUR", "amount": 50.00 }
                                        ]
                                      }
                                    }
                                    """
                    )
            )
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Invoice total calculated successfully",
                    content = @Content(
                            mediaType = MediaType.TEXT_PLAIN,
                            schema = @Schema(type = SchemaType.STRING, example = "158.82")
                    )
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "Validation or request error",
                    content = @Content(
                            mediaType = MediaType.TEXT_PLAIN,
                            schema = @Schema(type = SchemaType.STRING, example = "Error: Invalid currency code")
                    )
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Exchange rate not found for one or more currencies",
                    content = @Content(
                            mediaType = MediaType.TEXT_PLAIN,
                            schema = @Schema(type = SchemaType.STRING, example = "Error: Rate not found for currency: EUR")
                    )
            ),
            @APIResponse(
                    responseCode = "502",
                    description = "External exchange-rate provider error",
                    content = @Content(
                            mediaType = MediaType.TEXT_PLAIN,
                            schema = @Schema(type = SchemaType.STRING, example = "Error: External API unavailable")
                    )
            ),
            @APIResponse(
                    responseCode = "500",
                    description = "Unexpected server error",
                    content = @Content(
                            mediaType = MediaType.TEXT_PLAIN,
                            schema = @Schema(type = SchemaType.STRING, example = "Error: Internal server error")
                    )
            )
    })
    public Response calculateTotal(@Valid InvoiceRequest request) {
        LOG.infof("InvoiceResource:calculateTotal - request=%s", request);
        BigDecimal total = invoiceService.calculateTotal(request);
        LOG.infof("InvoiceResource:calculateTotal Invoice total calculated successfully: %s", total);

        return Response.ok(total.toString()).build();
    }
}
