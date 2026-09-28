package com.example.eCommBackendNew2.payment;

import java.math.BigDecimal;

public interface PaymentGateway {
    String getProviderName();
    String processPayment(Long purchaseId, BigDecimal amount, String method);
}
