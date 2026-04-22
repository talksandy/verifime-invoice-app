package com.verifime.client;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import com.verifime.config.ExchangeApiConfig;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@ApplicationScoped
public class HttpClientWrapperImpl implements HttpClientWrapper {

    private HttpClient httpClient;

    @Inject
    ExchangeApiConfig config;

    public HttpClientWrapperImpl() {
        // Default constructor for CDI
    }

    // For testing
    public HttpClientWrapperImpl(ExchangeApiConfig config) {
        this.config = config;
        init();
    }

    private void init() {
        if (httpClient == null) {
            httpClient = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofMillis(config.timeout()))
                    .build();
        }
    }

    @Override
    public HttpResponse<String> send(String url) throws IOException, InterruptedException {
        if (httpClient == null) {
            init();
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
