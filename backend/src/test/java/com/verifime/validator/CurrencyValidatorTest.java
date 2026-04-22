package com.verifime.validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyValidatorTest {

    private final CurrencyValidator validator = new CurrencyValidator();

    @Test
    void testIsValid_ValidCurrency() {
        assertTrue(validator.isValid("USD", null));
        assertTrue(validator.isValid("EUR", null));
        assertTrue(validator.isValid("GBP", null));
        assertTrue(validator.isValid("JPY", null));
    }

    @Test
    void testIsValid_InvalidCurrency() {
        assertFalse(validator.isValid("INVALID", null));
        assertFalse(validator.isValid("US", null)); // Too short
        assertFalse(validator.isValid("USDD", null)); // Too long
        assertFalse(validator.isValid("123", null)); // Numbers
    }

    @Test
    void testIsValid_Null() {
        assertFalse(validator.isValid(null, null));
    }

    @Test
    void testIsValid_Blank() {
        assertFalse(validator.isValid("", null));
        assertFalse(validator.isValid("   ", null));
    }

    @Test
    void testIsValid_LowerCase() {
        assertFalse(validator.isValid("usd", null)); // Currency codes are case sensitive, should be uppercase
    }
}
