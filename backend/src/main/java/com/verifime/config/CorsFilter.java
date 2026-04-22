package com.verifime.config;

import jakarta.ws.rs.container.*;
import jakarta.ws.rs.ext.Provider;

@Provider
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext request,
                       ContainerResponseContext response) {

        response.getHeaders().add("Access-Control-Allow-Origin", "http://localhost:3000");
        response.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
        response.getHeaders().add("Access-Control-Allow-Credentials", "true");
        response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(200);
        }
    }
}