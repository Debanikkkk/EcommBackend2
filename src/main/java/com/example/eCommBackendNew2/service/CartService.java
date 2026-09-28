package com.example.eCommBackendNew2.service;

import com.example.eCommBackendNew2.dto.CartResponse;
import com.example.eCommBackendNew2.dto.ProductResponse;
import com.example.eCommBackendNew2.entity.Cart;
import com.example.eCommBackendNew2.entity.Product;
import com.example.eCommBackendNew2.entity.User;
import com.example.eCommBackendNew2.repository.CartRepository;
import com.example.eCommBackendNew2.repository.ProductRepository;
import com.example.eCommBackendNew2.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository,
                      UserRepository userRepository,
                      ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public CartResponse getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createEmptyCart(userId));
        return toResponse(cart);
    }

    public CartResponse addProductToCart(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createEmptyCart(userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));

        if (product.getStock() <= 0) {
            throw new IllegalArgumentException("Product is out of stock: " + product.getName());
        }

        cart.getProducts().add(product);
        cart.setTotal(calculateTotal(cart.getProducts()));
        cartRepository.save(cart);

        return toResponse(cart);
    }

    public CartResponse removeProductFromCart(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user id: " + userId));

        cart.getProducts().removeIf(product -> product.getId().equals(productId));
        cart.setTotal(calculateTotal(cart.getProducts()));
        cartRepository.save(cart);

        return toResponse(cart);
    }

    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user id: " + userId));
        cart.getProducts().clear();
        cart.setTotal(BigDecimal.ZERO);
        cartRepository.save(cart);
    }

    private Cart createEmptyCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setStatus("ACTIVE");
        cart.setTotal(BigDecimal.ZERO);
        return cartRepository.save(cart);
    }

    private BigDecimal calculateTotal(Set<Product> products) {
        return products.stream()
                .map(Product::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private CartResponse toResponse(Cart cart) {
        List<ProductResponse> products = cart.getProducts().stream()
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        product.getStock()))
                .toList();

        return new CartResponse(
                cart.getId(),
                cart.getUser().getId(),
                cart.getTotal(),
                cart.getStatus(),
                products
        );
    }
}
