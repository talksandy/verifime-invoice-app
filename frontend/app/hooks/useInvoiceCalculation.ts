import { useState } from "react";
import { Dayjs } from "dayjs";
import { DATE_FORMATS, MESSAGES } from "../constants";
import { InvoiceLine } from "../types/invoice";
import { calculateInvoiceTotal } from "../services/invoiceService";

export interface InvoicePayload {
  invoice: {
    currency: string;
    date: string;
    lines: Array<{
      description: string;
      currency: string;
      amount: number;
    }>;
  };
}

export function useInvoiceCalculation() {
  const [total, setTotal] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const calculate = async (
    date: Dayjs,
    currency: string,
    lines: InvoiceLine[]
  ): Promise<void> => {
    // Clear previous results
    setTotal(null);
    setError(null);
    setLoading(true);

    try {
      const payload: InvoicePayload = {
        invoice: {
          currency,
          date: date.format(DATE_FORMATS.API),
          lines: lines.map((l) => ({
            description: l.description,
            currency: l.currency,
            amount: Number(parseFloat(l.amount).toFixed(2)),
          })),
        },
      };

      const result = await calculateInvoiceTotal(payload);
      setTotal(result);
    } catch (err: unknown) {
      setError(
        err instanceof Error ? err.message : MESSAGES.ERRORS.UNKNOWN_ERROR
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
