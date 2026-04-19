package com.verifime.service;

import com.verifime.client.ExchangeRateClient;
import com.verifime.dto.Invoice;
import com.verifime.dto.InvoiceLine;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class ExchangeRateService {

    @Inject
    ExchangeRateClient exchangeRateClient;

    public Map<String, BigDecimal> getExchangeRates(Invoice invoice) {

        String baseCurrency = invoice.getCurrency();
        LocalDate date = invoice.getDate();

        Set<String> targetCurrencies = invoice.getLines().stream()
                .map(InvoiceLine::getCurrency)
                .filter(currency -> !currency.equalsIgnoreCase(baseCurrency))
                .collect(Collectors.toSet());

        String targetCurrency = String.join(",", targetCurrencies);

        if (targetCurrency.isEmpty()) {
            return null;
        }

        Map<String, BigDecimal> rates = exchangeRateClient.getRates(baseCurrency, targetCurrency, date);
        return rates;
    }

}
