package com.verifime.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResilientExchangeRateClientTest {

    @InjectMocks
    ResilientExchangeRateClient client;

    @Mock
    FrankfurterApiClient apiClient;

    @Test
    void testGetRates_Success() {
        String base = "USD";
        LocalDate date = LocalDate.of(2023, 10, 1);
        Map<String, BigDecimal> expectedRates = Map.of("EUR", new BigDecimal("0.85"));

        when(apiClient.getRates(base, date)).thenReturn(expectedRates);

        Map<String, BigDecimal> result = client.getRates(base, date);

        assertEquals(expectedRates, result);
        verify(apiClient).getRates(base, date);
    }

    @Test
    void testGetRates_Fallback() {
        String base = "USD";
        LocalDate date = LocalDate.of(2023, 10, 1);

        when(apiClient.getRates(base, date)).thenThrow(new RuntimeException("API error"));

        assertThrows(RuntimeException.class, () -> client.getRates(base, date));
        verify(apiClient).getRates(base, date);
    }
}
