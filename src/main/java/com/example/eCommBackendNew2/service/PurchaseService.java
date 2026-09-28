package com.example.eCommBackendNew2.service;

import com.example.eCommBackendNew2.dto.ProductResponse;
import com.example.eCommBackendNew2.dto.PurchaseResponse;
import com.example.eCommBackendNew2.entity.Cart;
import com.example.eCommBackendNew2.entity.Product;
import com.example.eCommBackendNew2.entity.Purchase;
import com.example.eCommBackendNew2.entity.User;
import com.example.eCommBackendNew2.repository.CartRepository;
import com.example.eCommBackendNew2.repository.ProductRepository;
import com.example.eCommBackendNew2.repository.PurchaseRepository;
import com.example.eCommBackendNew2.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public PurchaseService(PurchaseRepository purchaseRepository,
                          UserRepository userRepository,
                          CartRepository cartRepository,
                          ProductRepository productRepository) {
        this.purchaseRepository = purchaseRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public PurchaseResponse checkoutCart(Long userId, List<Long> productIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user id: " + userId));

        Set<Product> selectedProducts = new HashSet<>();
        for (Long productId : productIds) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));
            if (!cart.getProducts().contains(product)) {
                throw new IllegalArgumentException("Product not present in cart: " + productId);
            }
            selectedProducts.add(product);
        }

        BigDecimal total = selectedProducts.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setTotal(total);
        purchase.setStatus("PENDING");
        purchase.setProducts(selectedProducts);

        Purchase savedPurchase = purchaseRepository.save(purchase);

        cart.getProducts().removeIf(selectedProducts::contains);
        cart.setTotal(calculateTotal(cart.getProducts()));
        cartRepository.save(cart);

        return toResponse(savedPurchase);
    }

    public List<PurchaseResponse> getPurchasesByUser(Long userId) {
        return purchaseRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    public PurchaseResponse getPurchaseById(Long id) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Purchase not found with id: " + id));
        return toResponse(purchase);
    }

    private BigDecimal calculateTotal(Set<Product> products) {
        return products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private PurchaseResponse toResponse(Purchase purchase) {
        List<ProductResponse> products = purchase.getProducts().stream()
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getStock()))
                .toList();

        return new PurchaseResponse(
                purchase.getId(),
                purchase.getUser().getId(),
                purchase.getTotal(),
                purchase.getStatus(),
                products
        );
    }
}
