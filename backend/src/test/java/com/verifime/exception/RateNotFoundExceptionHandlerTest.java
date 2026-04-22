package com.verifime.exception;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RateNotFoundExceptionHandlerTest {

    private final RateNotFoundExceptionHandler handler = new RateNotFoundExceptionHandler();

    @Test
    void testToResponse() {
        RateNotFoundException exception = new RateNotFoundException("Rate not found for currency: XYZ");

        Response response = handler.toResponse(exception);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("Error: Rate not found for currency: XYZ", response.getEntity());
        assertEquals("text/plain", response.getMediaType().toString());
    }
}
