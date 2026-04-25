import { useState } from 'react';
import { Dayjs } from 'dayjs';
import { DATE_FORMATS, MESSAGES } from '../constants';
import { calculateInvoiceTotal } from '../services/invoiceService';
import { InvoiceLine, InvoicePayload } from '../types/invoice';

export function useInvoiceCalculation() {
  const [total, setTotal] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const calculate = async (
    date: Dayjs,
    currency: string,
    lines: InvoiceLine[]
  ): Promise<void> => {
    setTotal(null);
    setError(null);
    setLoading(true);

    try {
      const payload: InvoicePayload = {
        invoice: {
          currency,
          date: date.format(DATE_FORMATS.API),
          lines: lines.map((line) => ({
            description: line.description,
            currency: line.currency,
            amount: Number(parseFloat(line.amount).toFixed(2)),
          })),
        },
      };

      const result = await calculateInvoiceTotal(payload);
      setTotal(result);
    } catch (error: unknown) {
      setError(
        error instanceof Error ? error.message : MESSAGES.ERRORS.UNKNOWN_ERROR
      );
    } finally {
      setLoading(false);
    }
  };

  const clearResults = () => {
    setTotal(null);
    setError(null);
  };

  return {
    total,
    error,
    loading,
    calculate,
    clearResults,
  };
}
