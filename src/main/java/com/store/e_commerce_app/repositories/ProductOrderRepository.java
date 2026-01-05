package com.store.e_commerce_app.repositories;

import com.store.e_commerce_app.entities.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {

    public List<ProductOrder> findByUserDltsUserId(Long userId);

    public ProductOrder findByOrderId(String orderId);

}
