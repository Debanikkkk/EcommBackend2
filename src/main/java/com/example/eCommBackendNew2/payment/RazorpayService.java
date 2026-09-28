package com.example.eCommBackendNew2.payment;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RazorpayService implements PaymentGateway {
    @Override
    public String getProviderName() {
        return "RAZORPAY";
    }

    @Override
    public String processPayment(Long purchaseId, BigDecimal amount, String method) {
        return "SUCCESS";
    }
}
