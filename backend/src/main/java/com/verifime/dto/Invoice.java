package com.verifime.dto;

import java.time.LocalDate;
import java.util.List;

public class Invoice {
    private String currency;
    private LocalDate date;
    private List<InvoiceLine> lines;

    public Invoice() {
    }

    public Invoice(String currency, LocalDate date, List<InvoiceLine> lines) {
        this.currency = currency;
        this.date = date;
        this.lines = lines;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<InvoiceLine> getLines() {
        return lines;
    }

    public void setLines(List<InvoiceLine> lines) {
        this.lines = lines;
    }
}
