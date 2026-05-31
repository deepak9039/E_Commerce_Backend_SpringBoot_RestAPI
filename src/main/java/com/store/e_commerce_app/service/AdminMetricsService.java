package com.store.e_commerce_app.service;

import com.store.e_commerce_app.dto.TopProductSalesDTO;
import com.store.e_commerce_app.dto.TotalRevenueDTO;
import com.store.e_commerce_app.entities.ProductOrder;
import com.store.e_commerce_app.repositories.ProductOrderRepository;
import com.store.e_commerce_app.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AdminMetricsService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductOrderRepository productOrderRepository;

    // Total products in system
    public long totalProducts() {
        return productRepository.count();
    }

    // Products for owner
    public long productCountForOwner(Long ownerId) {
        return productRepository.countByOwner_UserId(ownerId);
    }

    // Total orders in system
    public long totalOrders() {
        return productOrderRepository.count();
    }

    // Orders count for owner
    public long ordersCountForOwner(Long ownerId) {
        return productOrderRepository.findByProductOwnerId(ownerId).size();
    }

    // Total revenue for delivered orders in system
    public double totalRevenue() {
        TotalRevenueDTO dto = productOrderRepository.getTotalRevenue();
        return dto != null && dto.getTotalRevenue() != null ? dto.getTotalRevenue() : 0.0;
    }

    // Revenue for owner: sum price*quantity for DELIVERED orders where product.owner = ownerId
    public double revenueForOwner(Long ownerId) {
        double total = 0.0;
        var orders = productOrderRepository.findByProductOwnerId(ownerId);
        if (orders == null || orders.isEmpty()) return 0.0;
        for (var po : orders) {
            String status = po.getStatus();
            if (status != null && status.equalsIgnoreCase("DELIVERED")) {
                Double price = po.getPrice();
                Integer qty = po.getQuantity();
                if (price != null && qty != null) {
                    total += price.doubleValue() * qty.doubleValue();
                }
            }
        }
        return total;
    }

    // Recent orders ascending for owner
    public List<ProductOrder> recentOrdersForOwner(Long ownerId) {
        if (ownerId == null) return Collections.emptyList();
        return productOrderRepository.findByProductOwnerIdOrderByOrderDateDesc(ownerId);
    }

    // Recent orders ascending for all
    public List<ProductOrder> recentOrdersAll() {
        return productOrderRepository.findAllByOrderByOrderDateAsc();
    }

    // Top products for all
    public List<TopProductSalesDTO> topProducts(int limit) {
        List<TopProductSalesDTO> all = productOrderRepository.getTopSellingProducts();
        if (all == null) return Collections.emptyList();
        return all.size() <= limit ? all : all.subList(0, limit);
    }

    // Top products for owner
    public List<TopProductSalesDTO> topProductsByOwner(Long ownerId, int limit) {
        if (ownerId == null) return Collections.emptyList();
        List<TopProductSalesDTO> all = productOrderRepository.getTopSellingProductsByOwner(ownerId);
        if (all == null) return Collections.emptyList();
        return all.size() <= limit ? all : all.subList(0, limit);
    }
}
