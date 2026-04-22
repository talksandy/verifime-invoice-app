package com.verifime.service;

import com.verifime.client.ExchangeRateProvider;
import com.verifime.dto.Invoice;
import com.verifime.dto.InvoiceLine;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class ExchangeRateService {

    @Inject
    ExchangeRateProvider exchangeRateProvider;

    private static final Logger LOG = Logger.getLogger(ExchangeRateService.class);

    public Map<String, BigDecimal> getExchangeRates(Invoice invoice) {
        LOG.debug("ExchangeRateService:getExchangeRates - Fetching exchange rates");

        String baseCurrency = invoice.currency();
        LocalDate date = invoice.date();

        Set<String> targetCurrencies = invoice.lines().stream()
                .map(InvoiceLine::currency)
                .filter(currency -> !currency.equalsIgnoreCase(baseCurrency))
                .collect(Collectors.toSet());
        LOG.infof("ExchangeRateService:getExchangeRates - base=%s targets=%s date=%s",
                baseCurrency, targetCurrencies, date);

        if (targetCurrencies.isEmpty()) {
            return Collections.emptyMap();
        }

        String targetCurrency = String.join(",", targetCurrencies);

        return exchangeRateProvider.getRates(baseCurrency, targetCurrency, date);
    }
}
