import { Button, Box } from "@mui/material";
import InvoiceLineItem from "./InvoiceLineItem";
import { InvoiceLine } from "../types/invoice";

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
        <Box sx={{ display: "flex", flexDirection: "column", gap: 4 }}>
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

            <Button variant="outlined" onClick={onAdd} sx={{ alignSelf: "flex-start" }}>
                Add Line
            </Button>
        </Box>
    );
}