package com.verifime.util;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RoundingUtilTest {

    @Test
    void testRoundCurrency_RoundDown() {
        BigDecimal input = new BigDecimal("1.234");
        BigDecimal expected = new BigDecimal("1.23");
        assertEquals(expected, RoundingUtil.roundCurrency(input));
    }

    @Test
    void testRoundCurrency_RoundUp() {
        BigDecimal input = new BigDecimal("1.235");
        BigDecimal expected = new BigDecimal("1.24");
        assertEquals(expected, RoundingUtil.roundCurrency(input));
    }

    @Test
    void testRoundCurrency_ExactTwoDecimals() {
        BigDecimal input = new BigDecimal("1.23");
        BigDecimal expected = new BigDecimal("1.23");
        assertEquals(expected, RoundingUtil.roundCurrency(input));
    }

    @Test
    void testRoundCurrency_MoreDecimals() {
        BigDecimal input = new BigDecimal("1.23456");
        BigDecimal expected = new BigDecimal("1.23");
        assertEquals(expected, RoundingUtil.roundCurrency(input));
    }

    @Test
    void testRoundCurrency_NegativeNumber() {
        BigDecimal input = new BigDecimal("-1.235");
        BigDecimal expected = new BigDecimal("-1.24");
        assertEquals(expected, RoundingUtil.roundCurrency(input));
    }

    @Test
    void testRoundCurrency_Zero() {
        BigDecimal input = new BigDecimal("0");
        BigDecimal expected = new BigDecimal("0.00");
        assertEquals(expected, RoundingUtil.roundCurrency(input));
    }

    @Test
    void testRoundCurrency_LargeNumber() {
        BigDecimal input = new BigDecimal("123456.789");
        BigDecimal expected = new BigDecimal("123456.79");
        assertEquals(expected, RoundingUtil.roundCurrency(input));
    }

    @Test
    void testRoundCurrency_VeryLargeNumber() {
        BigDecimal input = new BigDecimal("999999999.999");
        BigDecimal expected = new BigDecimal("1000000000.00");
        assertEquals(expected, RoundingUtil.roundCurrency(input));
    }

    @Test
    void testRoundFx_RoundDown() {
        BigDecimal input = new BigDecimal("1.23454");
        BigDecimal expected = new BigDecimal("1.2345");
        assertEquals(expected, RoundingUtil.roundFx(input));
    }

    @Test
    void testRoundFx_RoundUp() {
        BigDecimal input = new BigDecimal("1.23455");
        BigDecimal expected = new BigDecimal("1.2346");
        assertEquals(expected, RoundingUtil.roundFx(input));
    }

    @Test
    void testRoundFx_ExactFourDecimals() {
        BigDecimal input = new BigDecimal("1.2345");
        BigDecimal expected = new BigDecimal("1.2345");
        assertEquals(expected, RoundingUtil.roundFx(input));
    }

    @Test
    void testRoundFx_MoreDecimals() {
        BigDecimal input = new BigDecimal("1.23456789");
        BigDecimal expected = new BigDecimal("1.2346");
        assertEquals(expected, RoundingUtil.roundFx(input));
    }

    @Test
    void testRoundFx_NegativeNumber() {
        BigDecimal input = new BigDecimal("-1.23455");
        BigDecimal expected = new BigDecimal("-1.2346");
        assertEquals(expected, RoundingUtil.roundFx(input));
    }

    @Test
    void testRoundFx_Zero() {
        BigDecimal input = new BigDecimal("0");
        BigDecimal expected = new BigDecimal("0.0000");
        assertEquals(expected, RoundingUtil.roundFx(input));
    }

    @Test
    void testRoundFx_LargeNumber() {
        BigDecimal input = new BigDecimal("123456.789123");
        BigDecimal expected = new BigDecimal("123456.7891");
        assertEquals(expected, RoundingUtil.roundFx(input));
    }

    @Test
    void testRoundFx_VeryLargeNumber() {
        BigDecimal input = new BigDecimal("999999999.99999");
        BigDecimal expected = new BigDecimal("1000000000.0000");
        assertEquals(expected, RoundingUtil.roundFx(input));
    }

    @Test
    void testRoundCurrency_VerySmallNumber() {
        BigDecimal input = new BigDecimal("0.001");
        BigDecimal expected = new BigDecimal("0.00");
        assertEquals(expected, RoundingUtil.roundCurrency(input));
    }

    @Test
    void testRoundFx_VerySmallNumber() {
        BigDecimal input = new BigDecimal("0.00001");
        BigDecimal expected = new BigDecimal("0.0000");
        assertEquals(expected, RoundingUtil.roundFx(input));
    }
}
