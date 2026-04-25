export type InvoiceLine = {
  description: string;
  amount: string;
  currency: string;
};

export type InvoicePayload = {
  invoice: {
    currency: string;
    date: string;
    lines: {
      description: string;
      currency: string;
      amount: number;
    }[];
  };
};

export type LineErrors = Array<{ amount?: string; currency?: string }>;
