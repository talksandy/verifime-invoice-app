/**
 * Application Constants
 * Centralized configuration for API, messages, and validation rules
 */

export const API = {
  TIMEOUT: 30000,
  RETRY_ATTEMPTS: 3,
  ENDPOINTS: {
    INVOICE_TOTAL: '/invoice/total',
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
    FETCH_ERROR: 'Failed to fetch from server',
    TIMEOUT_ERROR: 'Request timed out. Please try again.',
    NETWORK_ERROR: 'Network error. Please check your connection.',
    PARSE_ERROR: 'Failed to parse response from server',
    VALIDATION_ERROR: 'Please fix the validation errors',
    UNKNOWN_ERROR: 'An unexpected error occurred',
  },
  SUCCESS: {
    CALCULATION_SUCCESS: 'Calculation completed successfully',
  },
} as const;

export const VALIDATION_RULES = {
  CURRENCY_CODE_LENGTH: 3,
  CURRENCY_CODE_PATTERN: /^[A-Z]{3}$/,
  AMOUNT_MIN: 0,
  AMOUNT_DECIMAL_PLACES: 2,
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
  DISPLAY: 'MMM DD, YYYY',
  API: 'YYYY-MM-DD',
} as const;

export const HTTP_STATUS = {
  OK: 200,
  BAD_REQUEST: 400,
  UNAUTHORIZED: 401,
  FORBIDDEN: 403,
  NOT_FOUND: 404,
  SERVER_ERROR: 500,
} as const;

export const ERROR_TYPES = {
  VALIDATION: 'VALIDATION_ERROR',
  REQUEST: 'REQUEST_ERROR',
  TIMEOUT: 'TIMEOUT_ERROR',
  NETWORK: 'NETWORK_ERROR',
  SERVER: 'SERVER_ERROR',
  UNKNOWN: 'UNKNOWN_ERROR',
} as const;

