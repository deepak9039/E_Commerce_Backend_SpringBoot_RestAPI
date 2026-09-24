package com.store.e_commerce_app.repositories;

import com.store.e_commerce_app.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByProductId(Long productId);

    boolean existsByProductName(String productName);

    Page<Product> findBycategoryName(String categoryName, Pageable pageable);

    //Page
    Page<Product> findAll(Pageable pageable);

    Page<Product> findByProductNameContainingIgnoreCaseOrCategoryNameContainingIgnoreCase(String productName, String categoryName, Pageable pageable);

    List<Product> findByIsSponsoredTrue();

    // find products having discount greater than or equal to a percentage
    List<Product> findByDiscountGreaterThanEqual(Integer discount);

    // Products by owner id
    List<Product> findByOwner_UserId(Long ownerId);

    // Products NOT owned by an owner (useful if you need)
    List<Product> findByOwner_UserIdNot(Long ownerId);

    // Paged products by owner
    Page<Product> findByOwner_UserId(Long ownerId, Pageable pageable);

    // New: count products for owner
    long countByOwner_UserId(Long ownerId);
}
