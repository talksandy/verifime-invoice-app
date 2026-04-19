package com.verifime.dto;

import java.math.BigDecimal;

public class RateResponse {
    public String date;
    public String base;
    public String quote;
    public BigDecimal rate;

    public String getDate() {
        return date;
    }

    public String getBase() {
        return base;
    }

    public String getQuote() {
        return quote;
    }

    public BigDecimal getRate() {
        return rate;
    }
}
