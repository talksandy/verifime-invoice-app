'use client';

import { useState } from 'react';
import dayjs, { Dayjs } from 'dayjs';
import { Box, Button, CircularProgress, Paper, Typography } from '@mui/material';
import { BUTTON_LABELS } from '../constants';
import { useFormReset } from '../hooks/useFormReset';
import { useInvoiceCalculation } from '../hooks/useInvoiceCalculation';
import { useInvoiceValidation } from '../hooks/useInvoiceValidation';
import { InvoiceLine } from '../types/invoice';
import styles from '../styles/invoice-form.module.css';
import InvoiceHeader from './InvoiceHeader';
import InvoiceLines from './InvoiceLines';
import ResetButton from './ResetButton';
import ResultDisplay from './ResultDisplay';

export default function InvoiceForm() {
  const [date, setDate] = useState<Dayjs | null>(dayjs());
  const [currency, setCurrency] = useState('');
  const [lines, setLines] = useState<InvoiceLine[]>([
    { description: '', amount: '', currency: '' },
  ]);

  const {
    dateError,
    currencyError,
    lineErrors,
    validate,
    clearErrors,
  } = useInvoiceValidation();
  const {
    total,
    error,
    loading,
    calculate: calculateTotal,
    clearResults,
  } = useInvoiceCalculation();

  const { reset } = useFormReset({
    onDateChange: setDate,
    onCurrencyChange: setCurrency,
    onLinesChange: setLines,
    clearValidationErrors: clearErrors,
    clearResults,
  });

  const updateLine = (
    index: number,
    field: keyof InvoiceLine,
    value: string
  ) => {
    const updatedLines = [...lines];
    updatedLines[index][field] = value;
    setLines(updatedLines);
  };

  const addLine = () => {
    setLines([...lines, { description: '', amount: '', currency: '' }]);
  };

  const removeLine = (index: number) => {
    setLines(lines.filter((_, lineIndex) => lineIndex !== index));
  };

  const handleCalculate = async () => {
    const isValid = validate(date, currency, lines);
    if (!isValid) {
      return;
    }

    await calculateTotal(date!, currency, lines);
  };

  return (
    <Paper className={styles.formContainer}>
      <Typography variant="h5" gutterBottom>
        Invoice Calculator
      </Typography>

      <InvoiceHeader
        date={date}
        currency={currency}
        onDateChange={setDate}
        onCurrencyChange={setCurrency}
        dateError={dateError}
        currencyError={currencyError}
      />

      <InvoiceLines
        lines={lines}
        onChange={updateLine}
        onAdd={addLine}
        onRemove={removeLine}
        lineErrors={lineErrors}
      />

      <Box className={styles.buttonContainer}>
        <Button variant="contained" onClick={handleCalculate} disabled={loading}>
          {loading ? <CircularProgress size={20} /> : BUTTON_LABELS.CALCULATE_TOTAL}
        </Button>

        <ResetButton onReset={reset} />
      </Box>

      <ResultDisplay total={total} error={error} />
    </Paper>
  );
}
