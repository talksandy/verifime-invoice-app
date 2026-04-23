import { Alert } from "@mui/material";

type Props = {
    total: string | null;
    error: string | null;
};

export default function ResultDisplay({ total, error }: Props) {
    return (
        <>
            {total && <Alert severity="success">Total: {total}</Alert>}
            {error && <Alert severity="error">{error}</Alert>}
        </>
    );
}