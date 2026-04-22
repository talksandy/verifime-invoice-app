package com.verifime.client;

import com.verifime.client.mapper.FrankfurterResponseMapper;
import com.verifime.config.ExchangeApiConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FrankfurterApiClientTest {

    @InjectMocks
    FrankfurterApiClient client;

    @Mock
    ExchangeApiConfig config;

    @Mock
    FrankfurterResponseMapper responseMapper;

    @Mock
    HttpClientWrapper httpClientWrapper;

    @Test
    void testInstantiation() {
        // Basic test to ensure the class can be instantiated
        assertNotNull(client);
    }

    @Test
    void testBuildUrl() {
        when(config.baseUrl()).thenReturn("https://api.frankfurter.dev");
        when(config.endpoint()).thenReturn("/v2/rates");

        String base = "USD";
        LocalDate date = LocalDate.of(2023, 10, 1);

        String url = client.buildUrl(base, date);

        assertEquals("https://api.frankfurter.dev/v2/rates?base=USD&date=2023-10-01", url);
    }

    @Test
    void testGetRates_Success() throws Exception {
        when(config.baseUrl()).thenReturn("https://api.frankfurter.dev");
        when(config.endpoint()).thenReturn("/v2/rates");

        String base = "USD";
        LocalDate date = LocalDate.of(2023, 10, 1);
        String url = "https://api.frankfurter.dev/v2/rates?base=USD&date=2023-10-01";

        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn("{\"rates\":{\"EUR\":0.85}}");

        when(httpClientWrapper.send(url)).thenReturn(mockResponse);

        Map<String, BigDecimal> expectedRates = Map.of("EUR", new BigDecimal("0.85"));
        when(responseMapper.toRatesMap("{\"rates\":{\"EUR\":0.85}}")).thenReturn(expectedRates);

        Map<String, BigDecimal> result = client.getRates(base, date);

        assertEquals(expectedRates, result);
    }

    @Test
    void testGetRates_Non200Status() throws Exception {
        when(config.baseUrl()).thenReturn("https://api.frankfurter.dev");
        when(config.endpoint()).thenReturn("/v2/rates");

        String base = "USD";
        LocalDate date = LocalDate.of(2023, 10, 1);
        String url = "https://api.frankfurter.dev/v2/rates?base=USD&date=2023-10-01";

        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(404);

        when(httpClientWrapper.send(url)).thenReturn(mockResponse);

        assertThrows(RuntimeException.class, () -> client.getRates(base, date));
    }

    @Test
    void testGetRates_Exception() throws Exception {
        when(config.baseUrl()).thenReturn("https://api.frankfurter.dev");
        when(config.endpoint()).thenReturn("/v2/rates");

        String base = "USD";
        LocalDate date = LocalDate.of(2023, 10, 1);
        String url = "https://api.frankfurter.dev/v2/rates?base=USD&date=2023-10-01";

        when(httpClientWrapper.send(url)).thenThrow(new RuntimeException("Network error"));

        assertThrows(RuntimeException.class, () -> client.getRates(base, date));
    }
}
