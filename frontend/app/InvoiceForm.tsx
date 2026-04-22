'use client';

import { useState } from 'react';
import {
  Container,
  Typography,
  TextField,
  Button,
  Box,
  IconButton,
  Alert,
  Paper,
} from '@mui/material';
import { DatePicker } from '@mui/x-date-pickers';
import { Add, Remove } from '@mui/icons-material';
import dayjs, { Dayjs } from 'dayjs';

interface InvoiceLine {
  description: string;
  amount: string;
  currency: string;
}

interface ApiInvoiceLine {
  description: string;
  amount: number;
  currency: string;
}

interface InvoiceRequest {
  invoice: {
    currency: string;
    date: string; // ISO date
    lines: ApiInvoiceLine[];
  };
}

export default function InvoiceForm() {
  const [date, setDate] = useState<Dayjs | null>(null);
  const [currency, setCurrency] = useState('NZD');
  const [lines, setLines] = useState<InvoiceLine[]>([
    { description: '', amount: '', currency: 'USD' },
  ]);
  const [total, setTotal] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);

  const addLine = () => {
    setLines([...lines, { description: '', amount: '', currency: 'USD' }]);
  };

  const removeLine = (index: number) => {
    if (lines.length > 1) {
      setLines(lines.filter((_, i) => i !== index));
    }
  };

  const updateLine = (index: number, field: keyof InvoiceLine, value: string) => {
    const newLines = [...lines];
    newLines[index][field] = value;
    setLines(newLines);
  };

  const calculateTotal = async () => {
    setError(null);
    setTotal(null);
    setLoading(true);

    const payload: InvoiceRequest = {
      invoice: {
        currency: currency,
        date: date ? date.format('YYYY-MM-DD') : '',
        lines: lines.map(line => ({
          ...line,
          amount: parseFloat(line.amount) || 0,
        })),
      },
    };

    try {
      const response = await fetch('http://localhost:8080/invoice/total', {
        method: 'POST',
        mode: 'cors',
        headers: {
          'Content-Type': 'application/json',
            'Accept': 'text/plain',
        },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || 'Failed to calculate total');
      }

      const data = await response.json();
      setTotal(data.toString());
    } catch (err) {
      setError(err instanceof Error ? err.message : 'An error occurred');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container maxWidth="md" sx={{ py: 4 }}>
      <Typography variant="h4" component="h1" gutterBottom>
        Invoice Calculator
      </Typography>
      <Paper sx={{ p: 3 }}>
        <Box sx={{ display: 'flex', flexDirection: { xs: 'column', sm: 'row' }, gap: 3 }}>
          <Box sx={{ flex: 1 }}>
            <DatePicker
              label="Invoice Date"
              value={date}
              onChange={setDate}
              slotProps={{ textField: { fullWidth: true } }}
            />
          </Box>
          <Box sx={{ flex: 1 }}>
            <TextField
              fullWidth
              label="Currency"
              value={currency}
              onChange={(e) => setCurrency(e.target.value)}
            />
          </Box>
        </Box>

        <Typography variant="h6" sx={{ mt: 3, mb: 2 }}>
          Invoice Lines
        </Typography>
        {lines.map((line, index) => (
          <Box key={index} sx={{ mb: 2, p: 2, border: '1px solid #ddd', borderRadius: 1 }}>
            <Box sx={{ display: 'flex', flexDirection: { xs: 'column', sm: 'row' }, gap: 2 }}>
              <Box sx={{ flex: 1 }}>
                <TextField
                  fullWidth
                  label="Description"
                  value={line.description}
                  onChange={(e) => updateLine(index, 'description', e.target.value)}
                />
              </Box>
              <Box sx={{ flex: 1 }}>
                <TextField
                  fullWidth
                  label="Amount"
                  type="number"
                  value={line.amount}
                  onChange={(e) => updateLine(index, 'amount', e.target.value)}
                />
              </Box>
              <Box sx={{ flex: 1 }}>
                <TextField
                  fullWidth
                  label="Currency"
                  value={line.currency}
                  onChange={(e) => updateLine(index, 'currency', e.target.value)}
                />
              </Box>
              <Box sx={{ display: 'flex', alignItems: 'center' }}>
                <IconButton onClick={() => removeLine(index)} disabled={lines.length === 1}>
                  <Remove />
                </IconButton>
              </Box>
            </Box>
          </Box>
        ))}
        <Button startIcon={<Add />} onClick={addLine} variant="outlined">
          Add Line
        </Button>

        <Box sx={{ mt: 3 }}>
          <Button
            variant="contained"
            onClick={calculateTotal}
            disabled={loading}
            size="large"
          >
            {loading ? 'Calculating...' : 'Calculate Total'}
          </Button>
        </Box>

        {total && (
          <Alert severity="success" sx={{ mt: 2 }}>
            Total: {total} {currency}
          </Alert>
        )}
        {error && (
          <Alert severity="error" sx={{ mt: 2 }}>
            {error}
          </Alert>
        )}
      </Paper>
    </Container>
  );
}
