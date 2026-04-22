package com.verifime.exception;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IllegalArgumentExceptionHandlerTest {

    private final IllegalArgumentExceptionHandler handler = new IllegalArgumentExceptionHandler();

    @Test
    void testToResponse() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid input");

        Response response = handler.toResponse(exception);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Error: Invalid input", response.getEntity());
        assertEquals("text/plain", response.getMediaType().toString());
    }
}
