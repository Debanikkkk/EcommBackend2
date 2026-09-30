package com.example.eCommBackendNew2.controller;

import com.example.eCommBackendNew2.dto.PurchaseResponse;
import com.example.eCommBackendNew2.service.PurchaseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {
    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping("/checkout")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PurchaseResponse> checkout(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(purchaseService.checkoutCart(userId));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<PurchaseResponse>> getPurchasesByUser(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(purchaseService.getPurchasesByUser(userId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PurchaseResponse> getPurchaseById(@PathVariable Long id,
                                                            @AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(purchaseService.getPurchaseById(id, userId));
    }
}
