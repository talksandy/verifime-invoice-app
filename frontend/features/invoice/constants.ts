export const INVOICE_API = {
  ENDPOINTS: {
    TOTAL: '/invoice/total',
  },
} as const;

export const MESSAGES = {
  VALIDATION: {
    INVOICE_DATE_REQUIRED: 'Invoice date is required',
    BASE_CURRENCY_REQUIRED: 'Base currency is required',
    AMOUNT_REQUIRED: 'Amount is required',
    CURRENCY_REQUIRED: 'Currency is required',
  },
  ERRORS: {
    UNKNOWN_ERROR: 'An unexpected error occurred',
  },
  SUCCESS: {
    CALCULATION_SUCCESS: 'Calculation completed successfully',
  },
} as const;

export const VALIDATION_RULES = {
  CURRENCY_CODE_LENGTH: 3,
} as const;

export const FIELD_LABELS = {
  INVOICE_DATE: 'Invoice Date',
  BASE_CURRENCY: 'Base Currency',
  DESCRIPTION: 'Description',
  AMOUNT: 'Amount',
  CURRENCY: 'Currency',
} as const;

export const BUTTON_LABELS = {
  CALCULATE_TOTAL: 'Calculate Total',
  RESET: 'Reset',
  ADD_LINE: 'Add Line',
  DELETE: 'Delete',
  TRY_AGAIN: 'Try Again',
} as const;

export const DATE_FORMATS = {
  API: 'YYYY-MM-DD',
} as const;
