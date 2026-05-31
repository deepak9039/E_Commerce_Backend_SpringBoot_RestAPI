package com.store.e_commerce_app.controllers;

import com.store.e_commerce_app.config.CustomUser;
import com.store.e_commerce_app.dto.TopProductSalesDTO;
import com.store.e_commerce_app.entities.ProductOrder;
import com.store.e_commerce_app.service.AdminMetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/metrics/owner")
public class AdminMetricsController {

    @Autowired
    private AdminMetricsService adminMetricsService;

    @PostMapping("/productCount")
    public ResponseEntity<?> productCount(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }
        Object principal = authentication.getPrincipal();
        boolean isSuper = false;
        Long ownerId = null;
        if (principal instanceof CustomUser cu) {
            String role = cu.getRole();
            isSuper = role != null && (role.equalsIgnoreCase("SUPER_ADMIN") || role.equalsIgnoreCase("ROLE_SUPER_ADMIN"));
            ownerId = cu.getUserId();
        }
        long count = isSuper ? adminMetricsService.totalProducts() : (ownerId != null ? adminMetricsService.productCountForOwner(ownerId) : 0L);
        return ResponseEntity.ok(Map.of("message", "Success", "productCount", count));
    }

    @PostMapping("/ordersCount")
    public ResponseEntity<?> ordersCount(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }
        Object principal = authentication.getPrincipal();
        boolean isSuper = false;
        Long ownerId = null;
        if (principal instanceof CustomUser cu) {
            String role = cu.getRole();
            isSuper = role != null && (role.equalsIgnoreCase("SUPER_ADMIN") || role.equalsIgnoreCase("ROLE_SUPER_ADMIN"));
            ownerId = cu.getUserId();
        }
        long count = isSuper ? adminMetricsService.totalOrders() : (ownerId != null ? adminMetricsService.ordersCountForOwner(ownerId) : 0L);
        return ResponseEntity.ok(Map.of("message", "Success", "ordersCount", count));
    }

    @PostMapping("/revenue")
    public ResponseEntity<?> revenue(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }
        Object principal = authentication.getPrincipal();
        boolean isSuper = false;
        Long ownerId = null;
        if (principal instanceof CustomUser cu) {
            String role = cu.getRole();
            isSuper = role != null && (role.equalsIgnoreCase("SUPER_ADMIN") || role.equalsIgnoreCase("ROLE_SUPER_ADMIN"));
            ownerId = cu.getUserId();
        }
        double revenue = isSuper ? adminMetricsService.totalRevenue() : (ownerId != null ? adminMetricsService.revenueForOwner(ownerId) : 0.0);
        return ResponseEntity.ok(Map.of("message", "Success", "revenue", revenue));
    }

    // Recent orders ascending
    @PostMapping("/recentOrders")
    public ResponseEntity<?> recentOrders(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }
        Object principal = authentication.getPrincipal();
        boolean isSuper = false;
        Long ownerId = null;
        if (principal instanceof CustomUser cu) {
            String role = cu.getRole();
            isSuper = role != null && (role.equalsIgnoreCase("SUPER_ADMIN") || role.equalsIgnoreCase("ROLE_SUPER_ADMIN"));
            ownerId = cu.getUserId();
        }
        List<ProductOrder> orders = isSuper ? adminMetricsService.recentOrdersAll() : adminMetricsService.recentOrdersForOwner(ownerId);
        return ResponseEntity.ok(Map.of("message", "Success", "orders", orders));
    }

    // Top products
    @PostMapping("/topProducts")
    public ResponseEntity<?> topProducts(Authentication authentication, @RequestParam(defaultValue = "8") int limit) {
        if (authentication == null) {
            return ResponseEntity.status(401).body(Map.of("message", "Unauthorized"));
        }
        Object principal = authentication.getPrincipal();
        boolean isSuper = false;
        Long ownerId = null;
        if (principal instanceof CustomUser cu) {
            String role = cu.getRole();
            isSuper = role != null && (role.equalsIgnoreCase("SUPER_ADMIN") || role.equalsIgnoreCase("ROLE_SUPER_ADMIN"));
            ownerId = cu.getUserId();
        }
        List<TopProductSalesDTO> top = isSuper ? adminMetricsService.topProducts(limit) : adminMetricsService.topProductsByOwner(ownerId, limit);
        return ResponseEntity.ok(Map.of("message", "Success", "products", top));
    }
}
