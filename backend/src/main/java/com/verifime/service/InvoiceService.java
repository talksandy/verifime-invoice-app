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
import org.jboss.logging.Logger;

@ApplicationScoped
public class InvoiceService {

    @Inject
    ExchangeRateService exchangeRateService;

    @Inject
    CurrencyConverter currencyConverter;

    private static final Logger LOG = Logger.getLogger(InvoiceService.class);

    public BigDecimal calculateTotal(InvoiceRequest request) {
        LOG.debug("InvoiceService:calculateTotal - Starting invoice calculation");

        Invoice invoice = request.invoice();
        String baseCurrency = invoice.currency();
        LOG.debugf("InvoiceService:calculateTotal - Base currency=%s, date=%s",
                baseCurrency, invoice.date());
        Map<String, BigDecimal> rates = exchangeRateService.getExchangeRates(invoice);

        BigDecimal total = invoice.lines().stream()
                .map(line -> convertToCurrency(line, baseCurrency, rates))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        LOG.infof("InvoiceService:calculateTotal - Final total=%s", RoundingUtil.roundCurrency(total));
        return RoundingUtil.roundCurrency(total);
    }

    private BigDecimal convertToCurrency(
            InvoiceLine line,
            String baseCurrency,
            Map<String, BigDecimal> rates) {
        LOG.debugf("InvoiceService:convertToCurrency - Processing line desc=%s amount=%s currency=%s",
                line.description(), line.amount(), line.currency());
        if (line.currency().equalsIgnoreCase(baseCurrency)) {
            return currencyConverter.normalize(line.amount());
        }

        BigDecimal rate = rates.get(line.currency());

        if (rate == null) {
            LOG.errorf("InvoiceService:convertToCurrency - Missing rate for currency=%s",
                    line.currency());
            throw new RateNotFoundException("Rate not found for currency: " + line.currency());
        }

        return currencyConverter.convertToBase(line.amount(), rate);
    }
}