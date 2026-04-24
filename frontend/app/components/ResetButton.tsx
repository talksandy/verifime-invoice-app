"use client";

import { Button } from "@mui/material";
import RestartAltIcon from "@mui/icons-material/RestartAlt";
import { BUTTON_LABELS } from "../constants";
import styles from "../styles/components.module.css";

interface ResetButtonProps {
  onReset: () => void;
}

export default function ResetButton({ onReset }: ResetButtonProps) {
  return (
    <Button
      variant="outlined"
      size="small"
      startIcon={<RestartAltIcon />}
      onClick={onReset}
      className={styles.resetButton}
    >
      {BUTTON_LABELS.RESET}
    </Button>
  );
}
