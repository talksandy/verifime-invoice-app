package com.verifime.client.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.verifime.exception.ExchangeRateException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FrankfurterResponseMapperTest {

    private final FrankfurterResponseMapper mapper = new FrankfurterResponseMapper();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Inject ObjectMapper manually for testing
    public FrankfurterResponseMapperTest() {
        mapper.objectMapper = objectMapper;
    }

    @Test
    void testToRatesMap_ValidResponse() {
        String json = """
                [
                    {
                        "quote": "EUR",
                        "rate": 0.8543
                    },
                    {
                        "quote": "GBP",
                        "rate": 0.7321
                    }
                ]
                """;

        Map<String, BigDecimal> result = mapper.toRatesMap(json);

        assertEquals(2, result.size());
        assertEquals(new BigDecimal("0.8543"), result.get("EUR"));
        assertEquals(new BigDecimal("0.7321"), result.get("GBP"));
    }

    @Test
    void testToRatesMap_Rounding() {
        String json = """
                [
                    {
                        "quote": "EUR",
                        "rate": 0.85434567
                    }
                ]
                """;

        Map<String, BigDecimal> result = mapper.toRatesMap(json);

        assertEquals(1, result.size());
        assertEquals(new BigDecimal("0.8543"), result.get("EUR")); // Rounded to 4 decimals
    }

    @Test
    void testToRatesMap_EmptyArray() {
        String json = "[]";

        assertThrows(ExchangeRateException.class, () -> mapper.toRatesMap(json));
    }

    @Test
    void testToRatesMap_NullArray() {
        String json = "null";

        assertThrows(ExchangeRateException.class, () -> mapper.toRatesMap(json));
    }

    @Test
    void testToRatesMap_InvalidJson() {
        String json = "invalid json";

        assertThrows(RuntimeException.class, () -> mapper.toRatesMap(json));
    }

    @Test
    void testToRatesMap_SkipNullFields() {
        String json = """
                [
                    {
                        "quote": "EUR",
                        "rate": 0.85
                    },
                    {
                        "quote": null,
                        "rate": 0.75
                    },
                    {
                        "quote": "GBP",
                        "rate": null
                    }
                ]
                """;

        Map<String, BigDecimal> result = mapper.toRatesMap(json);

        assertEquals(1, result.size());
        assertEquals(new BigDecimal("0.8500"), result.get("EUR"));
    }
}
