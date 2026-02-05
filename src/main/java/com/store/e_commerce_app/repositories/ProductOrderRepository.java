package com.store.e_commerce_app.repositories;

import com.store.e_commerce_app.entities.ProductOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.store.e_commerce_app.dto.CategorySalesDTO;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {

    List<ProductOrder> findByUserDltsUserId(Long userId);

    ProductOrder findByOrderId(String orderId);

    // pageable query
    Page<ProductOrder> findByUserDltsUserId(Long userId, Pageable pageable);


    @Query("""
    SELECT new com.store.e_commerce_app.dto.CategorySalesDTO(
        p.categoryName,
        SUM(po.quantity)
    )
    FROM ProductOrder po
    JOIN po.product p
    WHERE po.status = 'DELIVERED'
    GROUP BY p.categoryName
    ORDER BY SUM(po.quantity) DESC
""")
    List<CategorySalesDTO> getCategoryWiseSales();


}
