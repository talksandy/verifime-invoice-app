package com.verifime.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.verifime.config.ExchangeApiConfig;
import com.verifime.dto.RateResponse;
import com.verifime.exception.ExchangeRateException;

import com.verifime.util.RoundingUtil;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class FrankfurterExchangeRateProvider implements ExchangeRateProvider {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    ExchangeApiConfig config;

    private HttpClient httpClient;

    @PostConstruct
    void init() {
        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(config.timeout()))
                .build();
    }

    @Override
    public Map<String, BigDecimal> getRates(String baseCurrency, String targetCurrencies, LocalDate date) {

        String url = String.format(
                "%s%s?base=%s&quotes=%s&date=%s",
                config.baseUrl(),
                config.endpoint(),
                baseCurrency,
                targetCurrencies,
                date
        );

        try {
            HttpResponse<String> response = sendRequest(url);

            if (response.statusCode() != 200) {
                throw new ExchangeRateException("API error: " + response.statusCode());
            }

            return parseRates(response.body());

        } catch (Exception e) {
            throw new ExchangeRateException("Failed to fetch exchange rates", e);
        }
    }


    private HttpResponse<String> sendRequest(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private Map<String, BigDecimal> parseRates(String responseBody) throws IOException {

        RateResponse[] ratesArray = objectMapper.readValue(responseBody, RateResponse[].class);

        if (ratesArray == null || ratesArray.length == 0) {
            throw new ExchangeRateException("No exchange rates returned from API");
        }

        Map<String, BigDecimal> rates = new HashMap<>();

        for (RateResponse rate : ratesArray) {
            if (rate.getQuote() == null || rate.getRate() == null) {
                continue; // skip bad data
            }
            rates.put(
                    rate.getQuote(),
                    RoundingUtil.roundFx(rate.getRate())
            );
        }
        return rates;
    }
}
