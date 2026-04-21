package com.verifime.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

    private static final Logger log = Logger.getLogger(GlobalExceptionHandler.class);

    @Override
    public Response toResponse(Throwable exception) {

        log.error("Unhandled exception", exception);

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error: Internal server error")
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}
