import { Button, Box } from "@mui/material";
import InvoiceLineItem from "./InvoiceLineItem";
import { BUTTON_LABELS } from "../constants";
import { InvoiceLine } from "../types/invoice";
import styles from "../styles/components.module.css";

type Props = {
    lines: InvoiceLine[];
    onChange: (index: number, field: keyof InvoiceLine, value: string) => void;
    onAdd: () => void;
    onRemove: (index: number) => void;
    lineErrors?: Array<{amount?: string, currency?: string}>;
};

export default function InvoiceLines({
                                         lines,
                                         onChange,
                                         onAdd,
                                         onRemove,
                                         lineErrors,
                                     }: Props) {
    return (
        <Box className={styles.linesContainer}>
            {lines.map((line, index) => (
                <InvoiceLineItem
                    key={index}
                    line={line}
                    index={index}
                    onChange={onChange}
                    onRemove={onRemove}
                    canDelete={lines.length > 1}
                    errors={lineErrors?.[index]}
                />
            ))}

            <Button variant="outlined" onClick={onAdd} className={styles.addLineButton}>
                {BUTTON_LABELS.ADD_LINE}
            </Button>
        </Box>
    );
}
