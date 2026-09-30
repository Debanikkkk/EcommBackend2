package com.example.eCommBackendNew2.controller;

import com.example.eCommBackendNew2.dto.CartResponse;
import com.example.eCommBackendNew2.dto.ProductResponse;
import com.example.eCommBackendNew2.service.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @GetMapping("/me/products")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<ProductResponse>> getProducts(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(cartService.getProductsByUserId(userId));
    }

    @PostMapping("/me/products/{productId}")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CartResponse> addProduct(@AuthenticationPrincipal Long userId, @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.addProductToCart(userId, productId));
    }

    @DeleteMapping("/me/products/{productId}")
    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<CartResponse> removeProduct(@AuthenticationPrincipal Long userId, @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.removeProductFromCart(userId, productId));
    }
}
