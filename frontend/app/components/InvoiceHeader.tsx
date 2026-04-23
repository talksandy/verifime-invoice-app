import { Box, TextField } from "@mui/material";
import { DatePicker } from "@mui/x-date-pickers/DatePicker";
import { Dayjs } from "dayjs";
import dayjs from "dayjs";

type Props = {
    date: Dayjs | null;
    currency: string;
    onDateChange: (value: Dayjs | null) => void;
    onCurrencyChange: (value: string) => void;
    dateError?: string | null;
    currencyError?: string | null;
};

export default function InvoiceHeader({
                                          date,
                                          currency,
                                          onDateChange,
                                          onCurrencyChange,
                                          dateError,
                                          currencyError,
                                      }: Props) {
    return (
        <Box sx={{ display: "flex", gap: 2, mb: 3 }}>
            <DatePicker 
                label="Invoice Date" 
                value={date} 
                onChange={onDateChange}
                maxDate={dayjs()}
                slotProps={{
                    textField: {
                        error: !!dateError,
                        helperText: dateError,
                    },
                }}
            />

            <TextField
                label="Base Currency"
                value={currency}
                onChange={(e) => onCurrencyChange(e.target.value.slice(0, 3))}
                sx={{ minWidth: 150 }}
                required
                error={!!currencyError}
                helperText={currencyError}
            />
        </Box>
    );
}