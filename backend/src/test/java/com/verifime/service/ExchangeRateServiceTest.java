package com.verifime.service;

import com.verifime.client.ExchangeRateProvider;
import com.verifime.dto.Invoice;
import com.verifime.dto.InvoiceLine;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class ExchangeRateServiceTest {

    @Inject
    ExchangeRateService service;

    @InjectMock
    ExchangeRateProvider provider;

    @InjectMock
    ExchangeRateFacade facade;

    @Test
    void testGetExchangeRates_NoTargetCurrencies() {
        Invoice invoice = new Invoice("USD", LocalDate.of(2023, 10, 1),
                List.of(new InvoiceLine("Item1", "USD", new BigDecimal("100"))));

        Map<String, BigDecimal> result = service.getExchangeRates(invoice);

        assertTrue(result.isEmpty());
        verify(facade, never()).getRates(anyString(), any());
    }

    @Test
    void testGetExchangeRates_WithTargetCurrencies() {
        Invoice invoice = new Invoice("USD", LocalDate.of(2023, 10, 1),
                List.of(
                        new InvoiceLine("Item1", "USD", new BigDecimal("100")),
                        new InvoiceLine("Item2", "EUR", new BigDecimal("50"))
                ));

        Map<String, BigDecimal> allRates = Map.of("EUR", new BigDecimal("0.85"), "GBP", new BigDecimal("0.75"));
        when(facade.getRates("USD", LocalDate.of(2023, 10, 1))).thenReturn(allRates);

        Map<String, BigDecimal> result = service.getExchangeRates(invoice);

        assertEquals(1, result.size());
        assertEquals(new BigDecimal("0.85"), result.get("EUR"));
        verify(facade).getRates("USD", LocalDate.of(2023, 10, 1));
    }

    @Test
    void testGetExchangeRates_MultipleTargetCurrencies() {
        Invoice invoice = new Invoice("USD", LocalDate.of(2023, 10, 1),
                List.of(
                        new InvoiceLine("Item1", "EUR", new BigDecimal("100")),
                        new InvoiceLine("Item2", "GBP", new BigDecimal("50")),
                        new InvoiceLine("Item3", "JPY", new BigDecimal("200"))
                ));

        Map<String, BigDecimal> allRates = Map.of(
                "EUR", new BigDecimal("0.85"),
                "GBP", new BigDecimal("0.75"),
                "JPY", new BigDecimal("110.0")
        );
        when(facade.getRates("USD", LocalDate.of(2023, 10, 1))).thenReturn(allRates);

        Map<String, BigDecimal> result = service.getExchangeRates(invoice);

        assertEquals(3, result.size());
        assertEquals(new BigDecimal("0.85"), result.get("EUR"));
        assertEquals(new BigDecimal("0.75"), result.get("GBP"));
        assertEquals(new BigDecimal("110.0"), result.get("JPY"));
    }

    @Test
    void testGetExchangeRates_SomeCurrenciesNotInRates() {
        Invoice invoice = new Invoice("USD", LocalDate.of(2023, 10, 1),
                List.of(
                        new InvoiceLine("Item1", "EUR", new BigDecimal("100")),
                        new InvoiceLine("Item2", "XYZ", new BigDecimal("50")) // XYZ not in rates
                ));

        Map<String, BigDecimal> allRates = Map.of("EUR", new BigDecimal("0.85"));
        when(facade.getRates("USD", LocalDate.of(2023, 10, 1))).thenReturn(allRates);

        Map<String, BigDecimal> result = service.getExchangeRates(invoice);

        assertEquals(1, result.size());
        assertEquals(new BigDecimal("0.85"), result.get("EUR"));
        assertFalse(result.containsKey("XYZ"));
    }
}
