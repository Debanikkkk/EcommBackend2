package com.example.eCommBackendNew2.payment;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StripeService implements PaymentGateway {
    @Override
    public String getProviderName() {
        return "STRIPE";
    }

    @Override
    public String processPayment(Long purchaseId, BigDecimal amount, String method) {
        return "SUCCESS";
    }
}
