package com.verifime.service;

import com.verifime.dto.Invoice;
import com.verifime.dto.InvoiceLine;
import com.verifime.dto.InvoiceRequest;
import com.verifime.exception.RateNotFoundException;
import com.verifime.util.RoundingUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.util.Map;


@ApplicationScoped
public class InvoiceService {

    @Inject
    ExchangeRateService exchangeRateService;

    @Inject
    CurrencyConverter currencyConverter;

    public BigDecimal calculateTotal(InvoiceRequest request) {
        if (request == null || request.getInvoice() == null || request.getInvoice().lines() == null || request.getInvoice().lines().isEmpty()) {
            return RoundingUtil.roundCurrency(BigDecimal.ZERO);
        }

        Invoice invoice = request.getInvoice();
        String baseCurrency = invoice.currency();

        Map<String, BigDecimal> rates = exchangeRateService.getExchangeRates(invoice);

        BigDecimal total = invoice.lines().stream()
                .map(line -> convertToCurrency(line, baseCurrency, rates))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return RoundingUtil.roundCurrency(total);
    }

    private BigDecimal convertToCurrency(
            InvoiceLine line,
            String baseCurrency,
            Map<String, BigDecimal> rates) {

        if (line.currency().equalsIgnoreCase(baseCurrency)) {
            return currencyConverter.normalize(line.amount());
        }

        BigDecimal rate = rates.get(line.currency());

        if (rate == null) {
            throw new RateNotFoundException("Rate not found for currency: " + line.currency());
        }

        return currencyConverter.convertToBase(line.amount(), rate);
    }
}