package com.verifime.exception;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExchangeRateExceptionHandlerTest {

    private final ExchangeRateExceptionHandler handler = new ExchangeRateExceptionHandler();

    @Test
    void testToResponse() {
        ExchangeRateException exception = new ExchangeRateException("API unavailable");

        Response response = handler.toResponse(exception);

        assertEquals(Response.Status.BAD_GATEWAY.getStatusCode(), response.getStatus());
        assertEquals("Error: API unavailable", response.getEntity());
        assertEquals("text/plain", response.getMediaType().toString());
    }
}
