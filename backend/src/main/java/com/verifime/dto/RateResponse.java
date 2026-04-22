package com.verifime.dto;

import java.math.BigDecimal;

public record RateResponse (
        String date,
        String base,
        String quote,
        BigDecimal rate
) {}
