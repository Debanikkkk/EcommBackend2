package com.example.eCommBackendNew2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentRequest(
        @NotNull(message = "Purchase id is required")
        Long purchaseId,

        @NotBlank(message = "Provider is required")
        String provider,

        @NotBlank(message = "Payment method is required")
        String method
) {
}
