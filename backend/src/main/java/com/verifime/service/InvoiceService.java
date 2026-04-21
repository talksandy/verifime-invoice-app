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
        if (request == null || request.getInvoice() == null || request.getInvoice().getLines() == null || request.getInvoice().getLines().isEmpty()) {
            return RoundingUtil.roundCurrency(BigDecimal.ZERO);
        }

        Invoice invoice = request.getInvoice();
        String baseCurrency = invoice.getCurrency();

        Map<String, BigDecimal> rates = exchangeRateService.getExchangeRates(invoice);

        BigDecimal total = invoice.getLines().stream()
                .map(line -> convertToCurrency(line, baseCurrency, rates))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return RoundingUtil.roundCurrency(total);
    }

/*    private BigDecimal convertToCurrency(InvoiceLine line, String baseCurrency, Map<String, BigDecimal> rates) {
        if (line.getCurrency().equalsIgnoreCase(baseCurrency)) {
            return RoundingUtil.roundCurrency(line.getAmount());
        }

        BigDecimal rate = rates.get(line.getCurrency());
        if (rate == null) {
            // This case should be handled properly in real production (e.g. throw exception if rate missing)
            // For now, we follow instructions and assume rate exists from single API call
            throw new RuntimeException("Missing rate for currency: " + line.getCurrency());

        }

        // Frankfurter API returns rates as 1 base = X target.
        // To get base from target: amount / rate
        BigDecimal converted = line.getAmount()
                .divide(rate, 10, RoundingMode.HALF_UP); // high precision

        return RoundingUtil.roundCurrency(converted); // 2 decimal here
    }*/

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