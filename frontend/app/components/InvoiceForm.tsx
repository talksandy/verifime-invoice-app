"use client";

import { useState } from "react";
import dayjs, { Dayjs } from "dayjs";
import { Paper, Typography, Button, CircularProgress, Box } from "@mui/material";

import InvoiceHeader from "./InvoiceHeader";
import InvoiceLines from "./InvoiceLines";
import ResultDisplay from "./ResultDisplay";
import ResetButton from "./ResetButton";

import { BUTTON_LABELS } from "../constants";
import { InvoiceLine } from "../types/invoice";
import { useInvoiceValidation } from "../hooks/useInvoiceValidation";
import { useInvoiceCalculation } from "../hooks/useInvoiceCalculation";
import { useFormReset } from "../hooks/useFormReset";

import styles from "../styles/components.module.css";

export default function InvoiceForm() {
    const [date, setDate] = useState<Dayjs | null>(dayjs());
    const [currency, setCurrency] = useState("");
    const [lines, setLines] = useState<InvoiceLine[]>([
        { description: "", amount: "", currency: "" },
    ]);

    // Custom hooks for validation and calculation
    const { dateError, currencyError, lineErrors, validate, clearErrors } = useInvoiceValidation();
    const { total, error, loading, calculate: calculateTotal, clearResults } = useInvoiceCalculation();

    // Form reset hook
    const { reset } = useFormReset({
        onDateChange: setDate,
        onCurrencyChange: setCurrency,
        onLinesChange: setLines,
        clearValidationErrors: clearErrors,
        clearResults,
    });

    const updateLine = (index: number, field: keyof InvoiceLine, value: string) => {
        const updated = [...lines];
        updated[index][field] = value;
        setLines(updated);
    };

    const addLine = () => {
        setLines([...lines, { description: "", amount: "", currency: "" }]);
    };

    const removeLine = (index: number) => {
        setLines(lines.filter((_, i) => i !== index));
    };

    const handleCalculate = async () => {
        // Validate before calculating
        const isValid = validate(date, currency, lines);
        if (!isValid) return;

        // If validation passes, calculate total
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
                <Button 
                    variant="contained" 
                    onClick={handleCalculate} 
                    disabled={loading}
                >
                    {loading ? <CircularProgress size={20} /> : BUTTON_LABELS.CALCULATE_TOTAL}
                </Button>
                
                <ResetButton onReset={reset} />
            </Box>

            <ResultDisplay total={total} error={error} />
        </Paper>
    );
}
