import { useState } from 'react';
import { Dayjs } from 'dayjs';
import { MESSAGES } from '../constants';
import { InvoiceLine, LineErrors } from '../types/invoice';

export interface ValidationErrors {
  dateError: string | null;
  currencyError: string | null;
  lineErrors: LineErrors;
}

export function useInvoiceValidation() {
  const [dateError, setDateError] = useState<string | null>(null);
  const [currencyError, setCurrencyError] = useState<string | null>(null);
  const [lineErrors, setLineErrors] = useState<LineErrors>([{}]);

  const validate = (
    date: Dayjs | null,
    currency: string,
    lines: InvoiceLine[]
  ): boolean => {
    setDateError(null);
    setCurrencyError(null);
    setLineErrors(lines.map(() => ({})));

    let hasErrors = false;

    if (!date) {
      setDateError(MESSAGES.VALIDATION.INVOICE_DATE_REQUIRED);
      hasErrors = true;
    }

    if (!currency.trim()) {
      setCurrencyError(MESSAGES.VALIDATION.BASE_CURRENCY_REQUIRED);
      hasErrors = true;
    }

    const newLineErrors = lines.map((line) => {
      const errors: { amount?: string; currency?: string } = {};

      if (!line.amount.trim()) {
        errors.amount = MESSAGES.VALIDATION.AMOUNT_REQUIRED;
        hasErrors = true;
      }

      if (!line.currency.trim()) {
        errors.currency = MESSAGES.VALIDATION.CURRENCY_REQUIRED;
        hasErrors = true;
      }

      return errors;
    });

    setLineErrors(newLineErrors);

    return !hasErrors;
  };

  const clearErrors = () => {
    setDateError(null);
    setCurrencyError(null);
    setLineErrors([{}]);
  };

  return {
    dateError,
    currencyError,
    lineErrors,
    validate,
    clearErrors,
  };
}
