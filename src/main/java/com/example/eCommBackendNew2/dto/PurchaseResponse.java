package com.example.eCommBackendNew2.dto;

import java.math.BigDecimal;
import java.util.List;

public record PurchaseResponse(
        Long id,
        Long userId,
        BigDecimal total,
        String status,
        List<ProductResponse> products
) {
}
