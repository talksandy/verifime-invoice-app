package com.verifime.cache;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExchangeRateCacheTest {

    private final ExchangeRateCache cache = new ExchangeRateCache();

    @Test
    void testGet_ExistingKey() {
        String key = "USD:2023-10-01";
        Map<String, BigDecimal> rates = Map.of("EUR", new BigDecimal("0.85"));

        cache.put(key, rates);

        Map<String, BigDecimal> result = cache.get(key);

        assertEquals(rates, result);
    }

    @Test
    void testGet_NonExistingKey() {
        String key = "USD:2023-10-01";

        Map<String, BigDecimal> result = cache.get(key);

        assertNull(result);
    }

    @Test
    void testPut_AndGet() {
        String key = "EUR:2023-10-01";
        Map<String, BigDecimal> rates = Map.of("USD", new BigDecimal("1.18"));

        cache.put(key, rates);

        Map<String, BigDecimal> result = cache.get(key);

        assertEquals(rates, result);
    }

    @Test
    void testPut_Overwrite() {
        String key = "GBP:2023-10-01";
        Map<String, BigDecimal> rates1 = Map.of("USD", new BigDecimal("1.20"));
        Map<String, BigDecimal> rates2 = Map.of("USD", new BigDecimal("1.25"));

        cache.put(key, rates1);
        cache.put(key, rates2);

        Map<String, BigDecimal> result = cache.get(key);

        assertEquals(rates2, result);
    }
}
