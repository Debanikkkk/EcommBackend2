package com.example.eCommBackendNew2.service;

import com.example.eCommBackendNew2.dto.PaymentResponse;
import com.example.eCommBackendNew2.entity.Payment;
import com.example.eCommBackendNew2.entity.Purchase;
import com.example.eCommBackendNew2.payment.PaymentGateway;
import com.example.eCommBackendNew2.repository.PaymentRepository;
import com.example.eCommBackendNew2.repository.PurchaseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PurchaseRepository purchaseRepository;
    private final List<PaymentGateway> paymentGateways;

    public PaymentService(PaymentRepository paymentRepository,
                         PurchaseRepository purchaseRepository,
                         List<PaymentGateway> paymentGateways) {
        this.paymentRepository = paymentRepository;
        this.purchaseRepository = purchaseRepository;
        this.paymentGateways = paymentGateways;
    }

    public PaymentResponse processPayment(Long purchaseId, String provider, String method) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException("Purchase not found with id: " + purchaseId));

        PaymentGateway gateway = paymentGateways.stream()
                .filter(item -> item.getProviderName().equalsIgnoreCase(provider))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unsupported payment provider: " + provider));

        String status = gateway.processPayment(purchaseId, purchase.getTotal(), method);

        Payment payment = new Payment();
        payment.setPurchase(purchase);
        payment.setAmount(purchase.getTotal());
        payment.setStatus(status);

        Payment savedPayment = paymentRepository.save(payment);

        purchase.setStatus("PAID");
        purchaseRepository.save(purchase);

        return new PaymentResponse(
                savedPayment.getId(),
                savedPayment.getPurchase().getId(),
                savedPayment.getAmount(),
                savedPayment.getStatus(),
                gateway.getProviderName()
        );
    }
}
