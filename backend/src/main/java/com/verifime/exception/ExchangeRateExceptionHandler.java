package com.verifime.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ExchangeRateExceptionHandler implements ExceptionMapper<ExchangeRateException> {

    @Override
    public Response toResponse(ExchangeRateException exception) {
        return Response.status(Response.Status.BAD_GATEWAY)
                .entity("Error: " + exception.getMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}