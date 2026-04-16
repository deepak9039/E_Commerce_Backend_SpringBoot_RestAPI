package com.store.e_commerce_app.service;

import com.store.e_commerce_app.entities.Product;
import com.store.e_commerce_app.entities.RecentView;
import com.store.e_commerce_app.entities.UserDlts;
import com.store.e_commerce_app.repositories.RecentViewRepository;
import com.store.e_commerce_app.repositories.UserDltsRepository;
import com.store.e_commerce_app.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecentViewService {

    @Autowired
    private RecentViewRepository recentViewRepository;

    @Autowired
    private UserDltsRepository userDltsRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public void addRecentView(Long userId, Long productId) {
        UserDlts user = userDltsRepository.findByUserId(userId);
        if (user == null) return;
        Product product = productRepository.findByProductId(productId);
        if (product == null) return;

        // existing recent view entry -> update timestamp
        recentViewRepository.findByUserDltsUserIdAndProductProductId(userId, productId).ifPresentOrElse(rv -> {
            rv.setViewedAt(LocalDateTime.now());
            recentViewRepository.save(rv);
        }, () -> {
            RecentView rv = new RecentView(user, product, LocalDateTime.now());
            recentViewRepository.save(rv);
        });

        // Optionally trim to last N entries per user (not implemented here, can be done with native query or manual cleanup)
    }

    public List<RecentView> getRecentViews(Long userId, int limit) {
        List<RecentView> list = recentViewRepository.findByUserDltsUserIdOrderByViewedAtDesc(userId);
        if (limit > 0 && list.size() > limit) {
            return list.subList(0, limit);
        }
        return list;
    }
}

