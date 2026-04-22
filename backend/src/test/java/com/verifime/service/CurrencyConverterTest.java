package com.verifime.service;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class CurrencyConverterTest {

    private final CurrencyConverter converter = new CurrencyConverter();

    @Test
    void testConvertToBase_ValidConversion() {
        BigDecimal amount = new BigDecimal("100");
        BigDecimal rate = new BigDecimal("1.5");
        BigDecimal expected = new BigDecimal("66.67"); // 100 / 1.5 = 66.666..., rounded to 66.67
        assertEquals(expected, converter.convertToBase(amount, rate));
    }

    @Test
    void testConvertToBase_RateOne() {
        BigDecimal amount = new BigDecimal("100");
        BigDecimal rate = new BigDecimal("1");
        BigDecimal expected = new BigDecimal("100.00");
        assertEquals(expected, converter.convertToBase(amount, rate));
    }

    @Test
    void testConvertToBase_NullAmount() {
        assertThrows(IllegalArgumentException.class, () -> converter.convertToBase(null, new BigDecimal("1.5")));
    }

    @Test
    void testConvertToBase_NullRate() {
        assertThrows(IllegalArgumentException.class, () -> converter.convertToBase(new BigDecimal("100"), null));
    }

    @Test
    void testConvertToBase_ZeroRate() {
        assertThrows(IllegalArgumentException.class, () -> converter.convertToBase(new BigDecimal("100"), BigDecimal.ZERO));
    }

    @Test
    void testConvertToBase_NegativeRate() {
        BigDecimal amount = new BigDecimal("100");
        BigDecimal rate = new BigDecimal("-1.5");
        BigDecimal expected = new BigDecimal("-66.67");
        assertEquals(expected, converter.convertToBase(amount, rate));
    }

    @Test
    void testNormalize_ValidAmount() {
        BigDecimal amount = new BigDecimal("123.456");
        BigDecimal expected = new BigDecimal("123.46");
        assertEquals(expected, converter.normalize(amount));
    }

    @Test
    void testNormalize_ExactTwoDecimals() {
        BigDecimal amount = new BigDecimal("123.45");
        BigDecimal expected = new BigDecimal("123.45");
        assertEquals(expected, converter.normalize(amount));
    }

    @Test
    void testNormalize_NullAmount() {
        assertThrows(IllegalArgumentException.class, () -> converter.normalize(null));
    }

    @Test
    void testNormalize_Zero() {
        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal expected = new BigDecimal("0.00");
        assertEquals(expected, converter.normalize(amount));
    }
}
