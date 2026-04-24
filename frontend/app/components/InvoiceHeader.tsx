import { Box, TextField } from "@mui/material";
import { DatePicker } from "@mui/x-date-pickers/DatePicker";
import { Dayjs } from "dayjs";
import dayjs from "dayjs";
import { FIELD_LABELS, VALIDATION_RULES } from "../constants";
import styles from "../styles/components.module.css";

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
        <Box className={styles.headerRow}>
            <DatePicker
                label={FIELD_LABELS.INVOICE_DATE}
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
                label={FIELD_LABELS.BASE_CURRENCY}
                value={currency}
                onChange={(e) =>
                    onCurrencyChange(
                        e.target.value.slice(0, VALIDATION_RULES.CURRENCY_CODE_LENGTH)
                    )
                }
                sx={{ minWidth: 150 }}
                required
                error={!!currencyError}
                helperText={currencyError}
            />
        </Box>
    );
}
