package com.verifime.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
public class IllegalArgumentExceptionHandler implements ExceptionMapper<IllegalArgumentException> {

    private static final Logger LOG = Logger.getLogger(IllegalArgumentExceptionHandler.class);

    @Override
    public Response toResponse(IllegalArgumentException exception) {
        LOG.errorf("IllegalArgumentExceptionHandler:toResponse - Validation error message=%s",
                exception.getMessage());
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("Error: " + exception.getMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}
