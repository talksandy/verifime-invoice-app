package com.verifime.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.verifime.dto.RateResponse;
import com.verifime.util.RoundingUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class ExchangeRateClient {

    @Inject
    ObjectMapper objectMapper;

    private final HttpClient httpClient = HttpClient.newHttpClient();


    public Map<String, BigDecimal> getRates(String baseCurrency, String targetCurrencies, LocalDate date) {

        String url = String.format(
                "https://api.frankfurter.dev/v2/rates?base=%s&quotes=%s&date=%s",
                baseCurrency, targetCurrencies, date
        );

        try {
            HttpResponse<String> response = getHttpResponse(url);

            // Convert JSON array → DTO[]
            RateResponse[] ratesArray = objectMapper.readValue(response.body(), RateResponse[].class);

            Map<String, BigDecimal> rates = new HashMap<>();

            for (RateResponse rate : ratesArray) {
                rates.put(
                        rate.getQuote(),
                        RoundingUtil.roundFx(rate.getRate())
                );
            }

            return rates;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch exchange rates", e);
        }
    }

    private HttpResponse<String> getHttpResponse(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );
        return response;
    }
}
