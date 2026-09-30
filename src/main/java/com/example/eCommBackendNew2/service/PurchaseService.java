package com.example.eCommBackendNew2.service;

import com.example.eCommBackendNew2.dto.ProductResponse;
import com.example.eCommBackendNew2.dto.PurchaseResponse;
import com.example.eCommBackendNew2.entity.Cart;
import com.example.eCommBackendNew2.entity.Product;
import com.example.eCommBackendNew2.entity.Purchase;
import com.example.eCommBackendNew2.entity.User;
import com.example.eCommBackendNew2.repository.CartRepository;
import com.example.eCommBackendNew2.repository.PurchaseRepository;
import com.example.eCommBackendNew2.repository.UserRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
public class PurchaseService {
    private final SecurityFilterChain securityFilterChain;
    private final PurchaseRepository purchaseRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public PurchaseService(PurchaseRepository purchaseRepository,
                          UserRepository userRepository,
                                                  CartRepository cartRepository, SecurityFilterChain securityFilterChain) {
        this.purchaseRepository = purchaseRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.securityFilterChain = securityFilterChain;
    }

        @Transactional
        public PurchaseResponse checkoutCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user id: " + userId));

                Set<Product> selectedProducts = Set.copyOf(cart.getProducts());
                if (selectedProducts.isEmpty()) {
                        throw new IllegalArgumentException("Cannot checkout an empty cart");
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

        cart.getProducts().clear();
        cart.setTotal(BigDecimal.ZERO);
        cartRepository.save(cart);

        return toResponse(savedPurchase);
    }

    public List<PurchaseResponse> getPurchasesByUser(Long userId) {
        return purchaseRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

        public PurchaseResponse getPurchaseById(Long id, Long userId) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Purchase not found with id: " + id));
                if (!purchase.getUser().getId().equals(userId)) {
                        throw new IllegalArgumentException("Purchase not found with id: " + id);
                }
        return toResponse(purchase);
    }

    public PurchaseResponse cancelPurchase(Long purchaseId, Long userId){
        Purchase purchase=purchaseRepository.findById(purchaseId).orElseThrow(()->new IllegalArgumentException("Purchase not found"));
        
        if (!purchase.getUser().getId().equals(userId)){
                throw new IllegalArgumentException("the purchase is not found ");
        }

        if(purchase.getStatus().equals("CANCELED") || purchase.getStatus().equals("COMPLETED")){
                throw new IllegalArgumentException("PURCHASE MUST BE PENDING TO BE CANCELLED");
        }
        if(purchase.getStatus().equals("PENDING")){
                purchase.setStatus("CANCELED");
                purchaseRepository.save(purchase);
        }
        
        return toResponse(purchase);

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
