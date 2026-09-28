package com.example.eCommBackendNew2.repository;

import com.example.eCommBackendNew2.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
