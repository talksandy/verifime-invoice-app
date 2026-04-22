package com.verifime.service;

import com.verifime.cache.ExchangeRateCache;
import com.verifime.client.ResilientExchangeRateClient;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class ExchangeRateFacadeTest {

    @Inject
    ExchangeRateFacade facade;

    @InjectMock
    ExchangeRateCache cache;

    @InjectMock
    ResilientExchangeRateClient client;

    @Test
    void testGetRates_FromCache() {
        String base = "USD";
        LocalDate date = LocalDate.of(2023, 10, 1);
        String key = base + ":" + date;
        Map<String, BigDecimal> cachedRates = Map.of("EUR", new BigDecimal("0.85"));

        when(cache.get(key)).thenReturn(cachedRates);

        Map<String, BigDecimal> result = facade.getRates(base, date);

        assertEquals(cachedRates, result);
        verify(cache).get(key);
        verify(client, never()).getRates(anyString(), any());
        verify(cache, never()).put(anyString(), any());
    }

    @Test
    void testGetRates_FromApi() {
        String base = "USD";
        LocalDate date = LocalDate.of(2023, 10, 1);
        String key = base + ":" + date;
        Map<String, BigDecimal> apiRates = Map.of("EUR", new BigDecimal("0.85"));

        when(cache.get(key)).thenReturn(null);
        when(client.getRates(base, date)).thenReturn(apiRates);

        Map<String, BigDecimal> result = facade.getRates(base, date);

        assertEquals(apiRates, result);
        verify(cache).get(key);
        verify(client).getRates(base, date);
        verify(cache).put(key, apiRates);
    }

    @Test
    void testGetRates_EmptyMap() {
        String base = "USD";
        LocalDate date = LocalDate.of(2023, 10, 1);
        String key = base + ":" + date;
        Map<String, BigDecimal> apiRates = Map.of();

        when(cache.get(key)).thenReturn(null);
        when(client.getRates(base, date)).thenReturn(apiRates);

        Map<String, BigDecimal> result = facade.getRates(base, date);

        assertEquals(apiRates, result);
    }
}
