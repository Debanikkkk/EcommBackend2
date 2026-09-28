package com.example.eCommBackendNew2.controller;

import com.example.eCommBackendNew2.dto.PaymentResponse;
import com.example.eCommBackendNew2.service.PaymentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PaymentResponse> processPayment(@RequestBody @Valid com.example.eCommBackendNew2.dto.PaymentRequest request) {
        return ResponseEntity.ok(paymentService.processPayment(request.purchaseId(), request.provider(), request.method()));
    }
}
