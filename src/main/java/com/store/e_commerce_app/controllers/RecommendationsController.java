package com.store.e_commerce_app.controllers;

import com.store.e_commerce_app.dto.ProductDTO;
import com.store.e_commerce_app.entities.Product;
import com.store.e_commerce_app.entities.RecentView;
import com.store.e_commerce_app.service.RecommendationService;
import com.store.e_commerce_app.service.RecentViewService;
import com.store.e_commerce_app.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5174")
@RestController
@RequestMapping("/api/recommendations")
public class RecommendationsController {

    @Autowired
    private RecentViewService recentViewService;

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private ProductService productService;

    // Add a recent view (called when user views a product)
    @PostMapping("/recent-view")
    public ResponseEntity<?> addRecentView(@RequestParam Long userId, @RequestParam Long productId) {
        recentViewService.addRecentView(userId, productId);
        return ResponseEntity.ok().body("OK");
    }

    // Get recent views for a user
    @GetMapping("/recent-view")
    public ResponseEntity<?> getRecentViews(@RequestParam Long userId, @RequestParam(defaultValue = "10") int limit) {
        List<RecentView> list = recentViewService.getRecentViews(userId, limit);
        List<ProductDTO> products = list.stream()
                .map(RecentView::getProduct)
                .map(ProductDTO::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    // Get recommendations. If userId is provided (no productId), use user's last clicked product and return items similar to it.
    @GetMapping("/similar")
    public ResponseEntity<?> getSimilar(
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "8") int limit) {

        // If userId is provided (preference): find the user's last clicked product and return items similar to that product
        if (userId != null) {
            List<RecentView> last = recentViewService.getRecentViews(userId, 1);
            if (last == null || last.isEmpty() || last.get(0).getProduct() == null) {
                // no recent view found -> return empty list so UI can fallback to popular or prompt
                return ResponseEntity.ok(Collections.emptyList());
            }
            Long lastProductId = last.get(0).getProduct().getProductId();
            List<Product> result = recommendationService.findSimilarProducts(lastProductId, limit);
            List<ProductDTO> dto = result.stream().map(ProductDTO::from).collect(Collectors.toList());
            return ResponseEntity.ok(dto);
        }

        // Fallback: if productId is supplied, return items similar to that product
        if (productId != null) {
            List<Product> result = recommendationService.findSimilarProducts(productId, limit);
            List<ProductDTO> dto = result.stream().map(ProductDTO::from).collect(Collectors.toList());
            return ResponseEntity.ok(dto);
        }

        // Neither provided: instruct client
        return ResponseEntity.badRequest().body(Map.of("error", "Provide userId to get recommendations based on user's last click, or productId to get items similar to a product"));
    }

}
