package com.verifime.client;

import com.verifime.exception.ExchangeRateException;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@ApplicationScoped
public class ResilientExchangeRateClient implements ExchangeRateProvider {

    @Inject
    FrankfurterApiClient apiClient;

    private static final Logger LOG = Logger.getLogger(ResilientExchangeRateClient.class);

    @Retry
    @Timeout
    @CircuitBreaker
    @Fallback(fallbackMethod = "fallback")
    @Timed(value = "exchange_rate_provider_requests", description = "Time spent calling the exchange-rate provider")
    public Map<String, BigDecimal> getRates(String base, LocalDate date) {
        LOG.info("ResilientExchangeRateClient:getRates - Calling API ");
        return apiClient.getRates(base, date);
    }

    @Counted(value = "exchange_rate_provider_fallbacks_total", description = "Number of exchange-rate provider fallback invocations")
    public Map<String, BigDecimal> fallback(String base, LocalDate date) {

        throw new ExchangeRateException("External API unavailable");
    }
}
