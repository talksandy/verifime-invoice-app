package com.verifime.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class RateNotFoundExceptionHandler implements ExceptionMapper<RateNotFoundException> {

    @Override
    public Response toResponse(RateNotFoundException exception) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Error: " + exception.getMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}