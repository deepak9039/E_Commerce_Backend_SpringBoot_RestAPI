package com.store.e_commerce_app.repositories;

import com.store.e_commerce_app.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByUserUserIdAndOrderId(Long userId, String orderId);
}
