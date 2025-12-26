package com.store.e_commerce_app.repositories;

import com.store.e_commerce_app.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByProductId(Long productId);

    boolean existsByProductName(String productName);

    List<Product> findBycategoryName(String categoryName);

}
