import { Dayjs } from "dayjs";
import dayjs from "dayjs";
import { InvoiceLine } from "../types/invoice";

export interface UseFormResetProps {
  onDateChange: (date: Dayjs) => void;
  onCurrencyChange: (currency: string) => void;
  onLinesChange: (lines: InvoiceLine[]) => void;
  clearValidationErrors: () => void;
  clearResults: () => void;
}

export function useFormReset({
  onDateChange,
  onCurrencyChange,
  onLinesChange,
  clearValidationErrors,
  clearResults,
}: UseFormResetProps) {
  const reset = () => {
    // Reset form inputs
    onDateChange(dayjs());
    onCurrencyChange("");
    onLinesChange([{ description: "", amount: "", currency: "" }]);
    
    // Clear validation errors
    clearValidationErrors();
    
    // Clear calculation results
    clearResults();
  };

  return { reset };
}

