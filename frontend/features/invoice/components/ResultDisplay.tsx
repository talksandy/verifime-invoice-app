import { Alert } from '@mui/material';
import { MESSAGES } from '../constants';

type Props = {
  total: string | null;
  error: string | null;
};

export default function ResultDisplay({ total, error }: Props) {
  return (
    <>
      {total && (
        <Alert severity="success">
          {MESSAGES.SUCCESS.CALCULATION_SUCCESS}: {total}
        </Alert>
      )}
      {error && <Alert severity="error">{error}</Alert>}
    </>
  );
}
