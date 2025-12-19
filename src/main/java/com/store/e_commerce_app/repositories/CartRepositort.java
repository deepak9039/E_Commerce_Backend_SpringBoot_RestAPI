package com.store.e_commerce_app.repositories;

import com.store.e_commerce_app.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepositort extends JpaRepository<Cart, Long> {

    public Cart findByUserDltsUserIdAndProductProductId(Long productId, Long userId);

    public List<Cart> findByUserDltsUserId(Long userId);

    Integer countByUserDltsUserId(Long userDltsUserId);

    Cart findByIdAndUserDltsUserIdAndProductProductId(
            Long id,
            Long userId,
            Long productId
    );

}
