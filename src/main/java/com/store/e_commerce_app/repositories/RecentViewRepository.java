package com.store.e_commerce_app.repositories;

import com.store.e_commerce_app.entities.RecentView;
import com.store.e_commerce_app.entities.UserDlts;
import com.store.e_commerce_app.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecentViewRepository extends JpaRepository<RecentView, Long> {
    List<RecentView> findByUserDltsUserIdOrderByViewedAtDesc(Long userId);
    Optional<RecentView> findByUserDltsUserIdAndProductProductId(Long userId, Long productId);
}

