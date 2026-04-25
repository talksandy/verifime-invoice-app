import { apiClient } from '@/shared/api/api-client';
import { INVOICE_API } from '../constants';
import { InvoicePayload } from '../types/invoice';

export async function calculateInvoiceTotal(
  payload: InvoicePayload
): Promise<string> {
  return apiClient.post<string>(INVOICE_API.ENDPOINTS.TOTAL, payload, {
    headers: {
      Accept: 'text/plain',
    },
  });
}
