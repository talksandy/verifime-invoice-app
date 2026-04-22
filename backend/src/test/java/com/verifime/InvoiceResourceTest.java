package com.verifime;

import com.verifime.dto.Invoice;
import com.verifime.dto.InvoiceLine;
import com.verifime.dto.InvoiceRequest;
import com.verifime.service.InvoiceService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

@QuarkusTest
class InvoiceResourceTest {

    @InjectMock
    InvoiceService invoiceService;

    @Test
    void testCalculateTotalEndpoint() {
        Invoice invoice = new Invoice("USD", LocalDate.of(2023, 10, 1),
                List.of(
                        new InvoiceLine("Item1", "USD", new BigDecimal("100.00")),
                        new InvoiceLine("Item2", "EUR", new BigDecimal("50.00"))
                ));
        InvoiceRequest request = new InvoiceRequest(invoice);

        when(invoiceService.calculateTotal(request)).thenReturn(new BigDecimal("158.82"));

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/invoice/total")
                .then()
                .statusCode(200)
                .body(is("158.82"));
    }

    @Test
    void testCalculateTotalEndpoint_ValidationError() {
        // Invalid currency code
        Invoice invoice = new Invoice("INVALID", LocalDate.of(2023, 10, 1),
                List.of(new InvoiceLine("Item1", "USD", new BigDecimal("100.00"))));
        InvoiceRequest request = new InvoiceRequest(invoice);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/invoice/total")
                .then()
                .statusCode(400); // Bad Request due to validation
    }
}