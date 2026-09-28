package com.example.eCommBackendNew2.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CheckoutRequest(
        @NotEmpty(message = "At least one product is required")
        List<Long> productIds
) {
}
