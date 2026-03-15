package com.store.e_commerce_app.service;

import com.store.e_commerce_app.entities.ProductOrder;
import com.store.e_commerce_app.entities.Review;
import com.store.e_commerce_app.repositories.ProductOrderRepository;
import com.store.e_commerce_app.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductOrderRepository productOrderRepository;

    // only allow review if user has at least one delivered order for that product
    public Review createReview(Review review) {
        if (review == null || review.getUserDlts() == null || review.getProduct() == null) {
            throw new IllegalArgumentException("Invalid review payload");
        }

        Long userId = review.getUserDlts().getUserId();
        Long productId = review.getProduct().getProductId();

        // fetch all orders for user and check for delivered orders for the product
        List<ProductOrder> userOrders = productOrderRepository.findByUserDltsUserId(userId);
        boolean hasDelivered = false;
        if (userOrders != null) {
            for (ProductOrder po : userOrders) {
                if (po.getProduct() != null && productId.equals(po.getProduct().getProductId()) && "DELIVERED".equalsIgnoreCase(po.getStatus())) {
                    hasDelivered = true;
                    break;
                }
            }
        }

        if (!hasDelivered) {
            throw new IllegalStateException("User has not delivered orders for this product");
        }

        review.setCreatedAt(new Date());
        return reviewRepository.save(review);
    }

    public List<Review> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProductProductId(productId);
    }
}
