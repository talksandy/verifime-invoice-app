package com.verifime.service;

import com.verifime.util.RoundingUtil;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.math.MathContext;

@ApplicationScoped
public class CurrencyConverter {

    private static final MathContext MATH_CONTEXT = MathContext.DECIMAL64;

    private static final Logger LOG = Logger.getLogger(CurrencyConverter.class);

    /**
     * Converts amount from source currency to base currency
     *
     * @param amount amount in source currency
     * @param rate exchange rate (1 base = X source)
     * @return converted amount in base currency (rounded to 2 decimals)
     */
    public BigDecimal convertToBase(BigDecimal amount, BigDecimal rate) {
        LOG.debugf("CurrencyConverter:convertToBase - Converting amount=%s rate=%s",
                amount, rate);
        if (amount == null || rate == null) {
            throw new IllegalArgumentException("Amount and rate must not be null");
        }

        if (rate.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Rate cannot be zero");
        }

        // Convert: base = amount / rate
        BigDecimal converted = amount.divide(rate, MATH_CONTEXT);

        return RoundingUtil.roundCurrency(converted);
    }

    /**
     * No conversion needed, just rounding
     */
    public BigDecimal normalize(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount must not be null");
        }
        return RoundingUtil.roundCurrency(amount);
    }
}