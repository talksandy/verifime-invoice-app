import { Box, IconButton, TextField } from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import { BUTTON_LABELS, FIELD_LABELS, VALIDATION_RULES } from '../constants';
import { InvoiceLine } from '../types/invoice';
import styles from '../styles/invoice-form.module.css';

type Props = {
  line: InvoiceLine;
  index: number;
  onChange: (index: number, field: keyof InvoiceLine, value: string) => void;
  onRemove: (index: number) => void;
  canDelete: boolean;
  errors?: { amount?: string; currency?: string };
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
        onChange={(event) => onChange(index, 'description', event.target.value)}
      />

      <TextField
        label={FIELD_LABELS.AMOUNT}
        type="number"
        className={styles.amountField}
        value={line.amount}
        onChange={(event) => onChange(index, 'amount', event.target.value)}
        error={!!errors?.amount}
        helperText={errors?.amount}
      />

      <TextField
        label={FIELD_LABELS.CURRENCY}
        value={line.currency}
        onChange={(event) =>
          onChange(
            index,
            'currency',
            event.target.value.slice(0, VALIDATION_RULES.CURRENCY_CODE_LENGTH)
          )
        }
        className={styles.currencyField}
        required
        error={!!errors?.currency}
        helperText={errors?.currency}
      />

      {canDelete && (
        <IconButton
          aria-label={BUTTON_LABELS.DELETE}
          onClick={() => onRemove(index)}
        >
          <DeleteIcon />
        </IconButton>
      )}
    </Box>
  );
}
