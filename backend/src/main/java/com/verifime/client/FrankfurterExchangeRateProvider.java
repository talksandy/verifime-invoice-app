package com.verifime.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.verifime.config.ExchangeApiConfig;
import com.verifime.dto.RateResponse;
import com.verifime.exception.ExchangeRateException;

import com.verifime.service.ExchangeRateService;
import com.verifime.util.RoundingUtil;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

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

    private static final Logger LOG = Logger.getLogger(FrankfurterExchangeRateProvider.class);

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
        LOG.infof("FrankfurterExchangeRateProvider:getRates - Calling API url=%s", url);

        try {
            HttpResponse<String> response = sendRequest(url);
            LOG.debug("FrankfurterExchangeRateProvider:getRates - Response received from API");

            if (response.statusCode() != 200) {
                LOG.errorf("FrankfurterExchangeRateProvider:getRates - API returned status=%s",
                        response.statusCode());
                throw new ExchangeRateException("FrankfurterExchangeRateProvider API error: " + response.statusCode());
            }

            return parseRates(response.body());

        } catch (Exception exception) {
            LOG.errorf("FrankfurterExchangeRateProvider:getRates - Error while calling API: %s", exception.getMessage());
            throw new ExchangeRateException("Failed to fetch exchange rates from Frankfurter API", exception);
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
            if (rate.quote() == null || rate.rate() == null) {
                continue; // skip bad data
            }
            rates.put(
                    rate.quote(),
                    RoundingUtil.roundFx(rate.rate())
            );
        }
        return rates;
    }
}
