package com.store.e_commerce_app.repositories;

import com.store.e_commerce_app.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByProductId(Long productId);

    boolean existsByProductName(String productName);
}
