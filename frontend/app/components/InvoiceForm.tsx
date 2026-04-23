"use client";

import { useState } from "react";
import dayjs, { Dayjs } from "dayjs";
import { Paper, Typography, Button, CircularProgress } from "@mui/material";

import InvoiceHeader from "./InvoiceHeader";
import InvoiceLines from "./InvoiceLines";
import ResultDisplay from "./ResultDisplay";

import { InvoiceLine } from "../types/invoice";
import { calculateInvoiceTotal } from "../services/invoiceService";

export default function InvoiceForm() {
    const [date, setDate] = useState<Dayjs | null>(dayjs());
    const [currency, setCurrency] = useState("");
    const [lines, setLines] = useState<InvoiceLine[]>([
        { description: "", amount: "", currency: "" },
    ]);

    const [total, setTotal] = useState<string | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);

    // Validation errors
    const [dateError, setDateError] = useState<string | null>(null);
    const [currencyError, setCurrencyError] = useState<string | null>(null);
    const [lineErrors, setLineErrors] = useState<Array<{amount?: string, currency?: string}>>([{}]);

    const updateLine = (index: number, field: keyof InvoiceLine, value: string) => {
        const updated = [...lines];
        updated[index][field] = value;
        setLines(updated);
        setTotal(null);
    };

    const addLine = () => {
        setLines([...lines, { description: "", amount: "", currency: "" }]);
        setLineErrors([...lineErrors, {}]);
    };

    const removeLine = (index: number) => {
        setLines(lines.filter((_, i) => i !== index));
        setLineErrors(lineErrors.filter((_, i) => i !== index));
    };

    const calculate = async () => {
        // Reset previous errors
        setDateError(null);
        setCurrencyError(null);
        setLineErrors(lines.map(() => ({})));
        setError(null);
        setTotal(null);

        // Validation
        let hasErrors = false;

        if (!date) {
            setDateError("Invoice date is required");
            hasErrors = true;
        }

        if (!currency.trim()) {
            setCurrencyError("Base currency is required");
            hasErrors = true;
        }

        const newLineErrors = lines.map((line, index) => {
            const errors: {amount?: string, currency?: string} = {};
            if (!line.amount.trim()) {
                errors.amount = "Amount is required";
                hasErrors = true;
            }
            if (!line.currency.trim()) {
                errors.currency = "Currency is required";
                hasErrors = true;
            }
            return errors;
        });

        setLineErrors(newLineErrors);

        if (hasErrors) {
            return;
        }

        setLoading(true);

        try {
            const payload = {
                invoice: {
                    currency,
                    date: date!.format("YYYY-MM-DD"),
                    lines: lines.map((l) => ({
                        description: l.description,
                        currency: l.currency,
                        amount: Number(parseFloat(l.amount).toFixed(2)),
                    })),
                },
            };

            const result = await calculateInvoiceTotal(payload);
            setTotal(result);
        } catch (err: any) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <Paper sx={{ p: 4, maxWidth: 800, mx: "auto" }}>
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

            <Button variant="contained" onClick={calculate} disabled={loading} sx={{ mt: 3 }}>
                {loading ? <CircularProgress size={20} /> : "Calculate Total"}
            </Button>

            <ResultDisplay total={total} error={error} />
        </Paper>
    );
}