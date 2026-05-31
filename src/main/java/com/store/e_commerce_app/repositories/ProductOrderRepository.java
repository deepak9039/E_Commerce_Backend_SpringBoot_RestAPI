package com.store.e_commerce_app.repositories;

import com.store.e_commerce_app.dto.SalesOverviewDTO;
import com.store.e_commerce_app.dto.TopProductSalesDTO;
import com.store.e_commerce_app.dto.TotalRevenueDTO;
import com.store.e_commerce_app.entities.ProductOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.store.e_commerce_app.dto.CategorySalesDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {

    List<ProductOrder> findByUserDltsUserId(Long userId);

    ProductOrder findByOrderId(String orderId);

    //Product order by user ID and order ID
    ProductOrder findByUserDltsUserIdAndOrderId(Long userId, String orderId);

    // Latest products by ID DESC
    List<ProductOrder> findAllByOrderByIdDesc();

    Page<ProductOrder> findAll(Pageable pageable);

    // pageable query
    Page<ProductOrder> findByUserDltsUserId(Long userId, Pageable pageable);

    // New: find orders where the product's owner has given id
    @Query("SELECT po FROM ProductOrder po WHERE po.product.owner.userId = :ownerId ORDER BY po.orderDate DESC")
    List<ProductOrder> findByProductOwnerId(@Param("ownerId") Long ownerId);

    @Query("SELECT po FROM ProductOrder po WHERE po.product.owner.userId = :ownerId")
    Page<ProductOrder> findByProductOwnerId(@Param("ownerId") Long ownerId, Pageable pageable);

    // New: recent orders for owner ascending
    @Query("SELECT po FROM ProductOrder po WHERE po.product.owner.userId = :ownerId ORDER BY po.orderDate DESC")
    List<ProductOrder> findByProductOwnerIdOrderByOrderDateDesc(@Param("ownerId") Long ownerId);

    // New: recent orders for all owners ascending
    @Query("SELECT po FROM ProductOrder po ORDER BY po.orderDate ASC")
    List<ProductOrder> findAllByOrderByOrderDateAsc();

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

    @Query("""
    SELECT new com.store.e_commerce_app.dto.TopProductSalesDTO(
        p.productId,
        p.productName,
        SUM(po.quantity),
        SUM(po.quantity * po.price)
    )
    FROM ProductOrder po
    JOIN po.product p
    WHERE po.status = 'DELIVERED'
    GROUP BY p.productId, p.productName
    ORDER BY SUM(po.quantity) DESC
    """)
    List<TopProductSalesDTO> getTopSellingProducts();

    // New: top products scoped to owner
    @Query("""
    SELECT new com.store.e_commerce_app.dto.TopProductSalesDTO(
        p.productId,
        p.productName,
        SUM(po.quantity),
        SUM(po.quantity * po.price)
    )
    FROM ProductOrder po
    JOIN po.product p
    WHERE po.status = 'DELIVERED' AND p.owner.userId = :ownerId
    GROUP BY p.productId, p.productName
    ORDER BY SUM(po.quantity) DESC
    """)
    List<TopProductSalesDTO> getTopSellingProductsByOwner(@Param("ownerId") Long ownerId);

    @Query("""
    SELECT new com.store.e_commerce_app.dto.TotalRevenueDTO(
                SUM(po.quantity * po.price)
    )
    FROM ProductOrder po
    WHERE po.status = 'DELIVERED'
    """)
    TotalRevenueDTO getTotalRevenue();

    //sales overview by day of week (Monday, Tuesday, etc.) for the last 7 days
    //    @Query(value = """
    //    SELECT
    //        TO_CHAR(order_date, 'Day') AS day,
    //        COUNT(*) AS totalOrders,
    //        SUM(price * quantity) AS totalSales
    //    FROM product_order
    //    WHERE status = 'DELIVERED'
    //    GROUP BY TO_CHAR(order_date, 'Day'), EXTRACT(DOW FROM order_date)
    //    ORDER BY EXTRACT(DOW FROM order_date)
    //    """, nativeQuery = true)
    //        List<Object[]> getWeeklySalesOverview();
    @Query(value = """
    SELECT 
        TO_CHAR(d.day, 'Day') AS day,
        COALESCE(COUNT(po.id),0) AS totalOrders,
        COALESCE(SUM(po.price * po.quantity),0) AS totalSales
    FROM 
        generate_series(
            CURRENT_DATE - INTERVAL '6 days',
            CURRENT_DATE,
            INTERVAL '1 day'
        ) AS d(day)
    LEFT JOIN product_order po 
        ON DATE(po.order_date) = DATE(d.day)
        AND po.status = 'DELIVERED'
    GROUP BY d.day
    ORDER BY d.day
    """, nativeQuery = true)
        List<Object[]> getWeeklySalesOverview();

}
