package com.verifime.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class Invoice {
    @NotNull(message = "Currency must not be null")
    private String currency;
    @NotNull(message = "Date must not be null")
    private LocalDate date;
    @NotEmpty(message = "Invoice lines must not be empty")
    @Valid
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
