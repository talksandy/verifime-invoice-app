package com.verifime.exception;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testToResponse() {
        RuntimeException exception = new RuntimeException("Unexpected error");

        Response response = handler.toResponse(exception);

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        assertEquals("Error: Internal server error", response.getEntity());
        assertEquals("text/plain", response.getMediaType().toString());
    }
}
