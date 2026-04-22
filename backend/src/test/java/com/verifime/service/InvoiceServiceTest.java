package com.verifime.service;

import com.verifime.dto.Invoice;
import com.verifime.dto.InvoiceLine;
import com.verifime.dto.InvoiceRequest;
import com.verifime.exception.RateNotFoundException;
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
class InvoiceServiceTest {

    @Inject
    InvoiceService service;

    @InjectMock
    ExchangeRateService exchangeRateService;

    @InjectMock
    CurrencyConverter currencyConverter;

    @Test
    void testCalculateTotal_AllSameCurrency() {
        Invoice invoice = new Invoice("USD", LocalDate.of(2023, 10, 1),
                List.of(
                        new InvoiceLine("Item1", "USD", new BigDecimal("100.00")),
                        new InvoiceLine("Item2", "USD", new BigDecimal("50.00"))
                ));
        InvoiceRequest request = new InvoiceRequest(invoice);

        when(exchangeRateService.getExchangeRates(invoice)).thenReturn(Map.of());
        when(currencyConverter.normalize(new BigDecimal("100.00"))).thenReturn(new BigDecimal("100.00"));
        when(currencyConverter.normalize(new BigDecimal("50.00"))).thenReturn(new BigDecimal("50.00"));

        BigDecimal result = service.calculateTotal(request);

        assertEquals(new BigDecimal("150.00"), result);
        verify(exchangeRateService).getExchangeRates(invoice);
        verify(currencyConverter, times(2)).normalize(any());
        verify(currencyConverter, never()).convertToBase(any(), any());
    }

    @Test
    void testCalculateTotal_WithConversion() {
        Invoice invoice = new Invoice("USD", LocalDate.of(2023, 10, 1),
                List.of(
                        new InvoiceLine("Item1", "USD", new BigDecimal("100.00")),
                        new InvoiceLine("Item2", "EUR", new BigDecimal("50.00"))
                ));
        InvoiceRequest request = new InvoiceRequest(invoice);

        Map<String, BigDecimal> rates = Map.of("EUR", new BigDecimal("0.85"));
        when(exchangeRateService.getExchangeRates(invoice)).thenReturn(rates);
        when(currencyConverter.normalize(new BigDecimal("100.00"))).thenReturn(new BigDecimal("100.00"));
        when(currencyConverter.convertToBase(new BigDecimal("50.00"), new BigDecimal("0.85"))).thenReturn(new BigDecimal("58.82"));

        BigDecimal result = service.calculateTotal(request);

        assertEquals(new BigDecimal("158.82"), result);
        verify(exchangeRateService).getExchangeRates(invoice);
        verify(currencyConverter).normalize(new BigDecimal("100.00"));
        verify(currencyConverter).convertToBase(new BigDecimal("50.00"), new BigDecimal("0.85"));
    }

    @Test
    void testCalculateTotal_RateNotFound() {
        Invoice invoice = new Invoice("USD", LocalDate.of(2023, 10, 1),
                List.of(
                        new InvoiceLine("Item1", "USD", new BigDecimal("100.00")),
                        new InvoiceLine("Item2", "EUR", new BigDecimal("50.00"))
                ));
        InvoiceRequest request = new InvoiceRequest(invoice);

        when(exchangeRateService.getExchangeRates(invoice)).thenReturn(Map.of()); // No EUR rate
        when(currencyConverter.normalize(new BigDecimal("100.00"))).thenReturn(new BigDecimal("100.00"));

        assertThrows(RateNotFoundException.class, () -> service.calculateTotal(request));
    }

    @Test
    void testCalculateTotal_SingleLine() {
        Invoice invoice = new Invoice("USD", LocalDate.of(2023, 10, 1),
                List.of(new InvoiceLine("Item1", "USD", new BigDecimal("123.45"))));
        InvoiceRequest request = new InvoiceRequest(invoice);

        when(exchangeRateService.getExchangeRates(invoice)).thenReturn(Map.of());
        when(currencyConverter.normalize(new BigDecimal("123.45"))).thenReturn(new BigDecimal("123.45"));

        BigDecimal result = service.calculateTotal(request);

        assertEquals(new BigDecimal("123.45"), result);
    }

    @Test
    void testCalculateTotal_EmptyLines() {
        Invoice invoice = new Invoice("USD", LocalDate.of(2023, 10, 1), List.of());
        InvoiceRequest request = new InvoiceRequest(invoice);

        when(exchangeRateService.getExchangeRates(invoice)).thenReturn(Map.of());

        BigDecimal result = service.calculateTotal(request);

        assertEquals(new BigDecimal("0.00"), result);
    }
}
