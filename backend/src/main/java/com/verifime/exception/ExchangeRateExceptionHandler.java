package com.verifime.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

@Provider
public class ExchangeRateExceptionHandler implements ExceptionMapper<ExchangeRateException> {

    private static final Logger LOG = Logger.getLogger(ExchangeRateExceptionHandler.class);

    @Override
    public Response toResponse(ExchangeRateException exception) {
        LOG.errorf("ExchangeRateExceptionHandler:toResponse - Exchange error message=%s",
                exception.getMessage());
        return Response.status(Response.Status.BAD_GATEWAY)
                .entity("Error: " + exception.getMessage())
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}