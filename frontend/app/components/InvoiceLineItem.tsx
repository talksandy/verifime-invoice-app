import {
    Box,
    TextField,
    IconButton,
} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import { InvoiceLine } from "../types/invoice";

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
        <Box sx={{ display: "flex", gap: 2, mb: 2 }}>
            <TextField
                label="Description"
                sx={{ flex: 0.6 }}
                value={line.description}
                onChange={(e) => onChange(index, "description", e.target.value)}
            />

            <TextField
                label="Amount"
                type="number"
                sx={{ flex: 0.4 }}
                value={line.amount}
                onChange={(e) => onChange(index, "amount", e.target.value)}
                error={!!errors?.amount}
                helperText={errors?.amount}
            />

            <TextField
                label="Currency"
                value={line.currency}
                onChange={(e) => onChange(index, "currency", e.target.value.slice(0, 3))}
                sx={{ flex: 0.4 }}
                required
                error={!!errors?.currency}
                helperText={errors?.currency}
            />

            {canDelete && (
                <IconButton onClick={() => onRemove(index)}>
                    <DeleteIcon />
                </IconButton>
            )}
        </Box>
    );
}