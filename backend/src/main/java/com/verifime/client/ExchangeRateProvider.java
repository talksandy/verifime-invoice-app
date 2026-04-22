package com.verifime.client;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public interface ExchangeRateProvider {

    Map<String, BigDecimal> getRates(
            String baseCurrency,
            LocalDate date
    );
}
