package com.example.eCommBackendNew2.repository;

import com.example.eCommBackendNew2.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPurchaseId(Long purchaseId);
}
