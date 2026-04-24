import {
    Box,
    TextField,
    IconButton,
} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import { BUTTON_LABELS, FIELD_LABELS, VALIDATION_RULES } from "../constants";
import { InvoiceLine } from "../types/invoice";
import styles from "../styles/components.module.css";

type Props = {
    line: InvoiceLine;
    index: number;
    onChange: (index: number, field: keyof InvoiceLine, value: string) => void;
    onRemove: (index: number) => void;
    canDelete: boolean;
    errors?: {amount?: string, currency?: string};
};

export default function InvoiceLineItem({
                                            line,
                                            index,
                                            onChange,
                                            onRemove,
                                            canDelete,
                                            errors,
                                        }: Props) {
    return (
        <Box className={styles.lineItemContainer}>
            <TextField
                label={FIELD_LABELS.DESCRIPTION}
                className={styles.descriptionField}
                value={line.description}
                onChange={(e) => onChange(index, "description", e.target.value)}
            />

            <TextField
                label={FIELD_LABELS.AMOUNT}
                type="number"
                className={styles.amountField}
                value={line.amount}
                onChange={(e) => onChange(index, "amount", e.target.value)}
                error={!!errors?.amount}
                helperText={errors?.amount}
            />

            <TextField
                label={FIELD_LABELS.CURRENCY}
                value={line.currency}
                onChange={(e) =>
                    onChange(
                        index,
                        "currency",
                        e.target.value.slice(0, VALIDATION_RULES.CURRENCY_CODE_LENGTH)
                    )
                }
                className={styles.currencyField}
                required
                error={!!errors?.currency}
                helperText={errors?.currency}
            />

            {canDelete && (
                <IconButton aria-label={BUTTON_LABELS.DELETE} onClick={() => onRemove(index)}>
                    <DeleteIcon />
                </IconButton>
            )}
        </Box>
    );
}
