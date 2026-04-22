package com.verifime.dto;

import java.math.BigDecimal;
/**
 * NOTE:
 * This implementation uses a custom/transformed Frankfurter API response
 * where rates are returned as an array of RateResponse.
 * This differs from the standard Frankfurter API format.
 */
public record RateResponse (
        String date,
        String base,
        String quote,
        BigDecimal rate
) {}
