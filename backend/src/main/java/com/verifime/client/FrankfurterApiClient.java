package com.verifime.client;

import com.verifime.client.mapper.FrankfurterResponseMapper;
import com.verifime.config.ExchangeApiConfig;
import com.verifime.exception.ExchangeRateException;
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
import java.util.Map;

@ApplicationScoped
public class FrankfurterApiClient {

    @Inject
    ExchangeApiConfig config;

    private HttpClient httpClient;

    @Inject
    FrankfurterResponseMapper responseMapper;

    private static final Logger LOG = Logger.getLogger(FrankfurterApiClient.class);

    @PostConstruct
    void init() {
        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(config.timeout()))
                .build();
    }

    public Map<String, BigDecimal> getRates(String baseCurrency, LocalDate date) {

        String url = String.format(
                "%s%s?base=%s&date=%s",
                config.baseUrl(),
                config.endpoint(),
                baseCurrency,
                date
        );
        LOG.infof("FrankfurterApiClient:getRates - Calling API url=%s", url);

        return getParsedResponse(url);
    }

    private Map<String, BigDecimal> getParsedResponse(String url) {
        try {
            HttpResponse<String> response = sendRequest(url);
            LOG.debug("FrankfurterApiClient:getParsedResponse - Response received from API");

            if (response.statusCode() != 200) {
                LOG.errorf("FrankfurterApiClient:getParsedResponse - API returned status=%s",
                        response.statusCode());
                throw new ExchangeRateException("FrankfurterApiClient API error: " + response.statusCode());
            }

            return responseMapper.toRatesMap(response.body());

        } catch (Exception exception) {
            LOG.errorf("FrankfurterApiClient:getParsedResponse - Error while calling API: %s", exception.getMessage());
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
}
