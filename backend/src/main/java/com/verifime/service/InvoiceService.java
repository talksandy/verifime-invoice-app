package com.verifime.service;

import com.verifime.dto.InvoiceRequest;
import jakarta.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;

@ApplicationScoped
public class InvoiceService {

    public BigDecimal calculateTotal(InvoiceRequest request) {
        // Placeholder for calculation logic
        return BigDecimal.ZERO;
    }
}
