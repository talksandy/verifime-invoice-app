package com.verifime.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Currency;

public class CurrencyValidator implements ConstraintValidator<ValidCurrency, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.isBlank()) {
            return false; // handled by @NotBlank
        }

        try {
            Currency.getInstance(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
