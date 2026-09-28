package com.example.eCommBackendNew2.dto;

import java.math.BigDecimal;

public record PaymentResponse(
        Long id,
        Long purchaseId,
        BigDecimal amount,
        String status,
        String provider
) {
}
