import dayjs from 'dayjs';
import { act, renderHook } from '@testing-library/react';
import { MESSAGES } from '../../constants';
import { useInvoiceValidation } from '../useInvoiceValidation';

describe('useInvoiceValidation', () => {
  it('validates required fields', () => {
    const { result } = renderHook(() => useInvoiceValidation());

    let isValid = true;

    act(() => {
      isValid = result.current.validate(null, '', []);
    });

    expect(isValid).toBe(false);
    expect(result.current.dateError).toBe(
      MESSAGES.VALIDATION.INVOICE_DATE_REQUIRED
    );
    expect(result.current.currencyError).toBe(
      MESSAGES.VALIDATION.BASE_CURRENCY_REQUIRED
    );
    expect(result.current.lineErrors).toEqual([]);
  });

  it('validates line item requirements', () => {
    const { result } = renderHook(() => useInvoiceValidation());

    let isValid = true;

    act(() => {
      isValid = result.current.validate(dayjs(), 'USD', [
        { description: '', amount: '', currency: '' },
      ]);
    });

    expect(isValid).toBe(false);
    expect(result.current.dateError).toBeNull();
    expect(result.current.currencyError).toBeNull();
    expect(result.current.lineErrors).toEqual([
      {
        amount: MESSAGES.VALIDATION.AMOUNT_REQUIRED,
        currency: MESSAGES.VALIDATION.CURRENCY_REQUIRED,
      },
    ]);
  });

  it('passes with valid inputs', () => {
    const { result } = renderHook(() => useInvoiceValidation());

    let isValid = false;

    act(() => {
      isValid = result.current.validate(dayjs(), 'USD', [
        { description: 'Consulting', amount: '100', currency: 'EUR' },
      ]);
    });

    expect(isValid).toBe(true);
    expect(result.current.dateError).toBeNull();
    expect(result.current.currencyError).toBeNull();
    expect(result.current.lineErrors).toEqual([{}]);
  });

  it('clears existing errors', () => {
    const { result } = renderHook(() => useInvoiceValidation());

    act(() => {
      result.current.validate(null, '', [
        { description: '', amount: '', currency: '' },
      ]);
    });

    act(() => {
      result.current.clearErrors();
    });

    expect(result.current.dateError).toBeNull();
    expect(result.current.currencyError).toBeNull();
    expect(result.current.lineErrors).toEqual([{}]);
  });
});
