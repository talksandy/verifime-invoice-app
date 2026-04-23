import { InvoicePayload } from "../types/invoice"

const API_URL = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";

export async function calculateInvoiceTotal(
    payload: InvoicePayload
): Promise<string> {
    const response = await fetch(`${API_URL}/invoice/total`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Accept: "text/plain",
        },
        body: JSON.stringify(payload),
    });

    const text = await response.text();

    if (!response.ok) {
        throw new Error(text);
    }

    return text;
}