package com.store.e_commerce_app.repositories;

import com.store.e_commerce_app.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByProductId(Long productId);

    boolean existsByProductName(String productName);

    List<Product> findBycategoryName(String categoryName);

    //Page
    Page<Product> findAll(Pageable pageable);

    List<Product> findByProductNameContainingIgnoreCaseOrCategoryNameContainingIgnoreCase(String productName, String categoryName);

    List<Product> findByIsSponsoredTrue();


}
