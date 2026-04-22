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

    @Inject ExchangeRateProvider exchangeRateProvider;
    @Inject ExchangeRateFacade exchangeRateFacade;

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

        Map<String, BigDecimal> allRates = exchangeRateFacade.getRates(baseCurrency, date);

        return getTargetCurrencyRates(targetCurrencies, allRates);

    }

    private Map<String, BigDecimal> getTargetCurrencyRates(Set<String> targetCurrencies, Map<String, BigDecimal> allRates) {
        Map<String, BigDecimal> targetCurrencyRates = targetCurrencies.stream()
                .filter(allRates::containsKey)
                .collect(Collectors.toMap(
                        currency -> currency,
                        allRates::get
                ));
        LOG.infof("ExchangeRateService:getTargetCurrencyRates - target currencies are : %s", targetCurrencyRates);
        return targetCurrencyRates;
    }
}
