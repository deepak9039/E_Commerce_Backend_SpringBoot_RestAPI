package com.store.e_commerce_app.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.e_commerce_app.entities.Review;
import com.store.e_commerce_app.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Value("${app.upload.review.dir:src/main/resources/static/review_img}")
    private String reviewUploadDir;

    // Create review with optional image
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createReview(
            @RequestPart("review") Review review,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        try {

            ResponseEntity<?> validation = validateReviewRequest(review);
            if (validation != null) return validation;

            // Create directory if not exists
            Path uploadPath = Paths.get(reviewUploadDir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            if (image != null && !image.isEmpty()) {

                String original = StringUtils.cleanPath(image.getOriginalFilename());
                if (original.contains("..")) {
                    return ResponseEntity.badRequest()
                            .body(Map.of("message", "Invalid image filename"));
                }

                String filename = System.currentTimeMillis() + "_" + original;

                Path target = uploadPath.resolve(filename);

                Files.copy(image.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

                // store filename
                review.setImageUrl(filename);

            } else {

                // default image if none uploaded
                if (review.getImageUrl() == null || review.getImageUrl().isBlank()) {
                    review.setImageUrl("default.png");
                }
            }

            Review saved = reviewService.createReview(review);

            return ResponseEntity.ok(
                    Map.of(
                            "message", "Review saved successfully",
                            "status", "success",
                            "review", saved
                    )
            );

        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to save image"));
        }
    }

    // Create review with JSON only (no image)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createReviewJson(@RequestBody Review review) {

        ResponseEntity<?> validation = validateReviewRequest(review);
        if (validation != null) return validation;

        if (review.getImageUrl() == null || review.getImageUrl().isBlank()) {
            review.setImageUrl("default.png");
        }

        Review saved = reviewService.createReview(review);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Review saved successfully",
                        "status", "success",
                        "review", saved
                )
        );
    }

    // Get reviews by product
    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getReviewsByProduct(@PathVariable Long productId) {

        List<Review> reviews = reviewService.getReviewsByProductId(productId);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Success",
                        "reviews", reviews
                )
        );
    }

    // validation
    private ResponseEntity<?> validateReviewRequest(Review review) {

        if (review == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Review payload is missing"));
        }

        if (review.getUserDlts() == null || review.getUserDlts().getUserId() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "userDlts.userId is required"));
        }

        if (review.getProduct() == null || review.getProduct().getProductId() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "product.productId is required"));
        }

        if (review.getRating() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "rating is required"));
        }

        int r = review.getRating();

        if (r < 1 || r > 5) {
            return ResponseEntity.badRequest().body(Map.of("message", "rating must be between 1 and 5"));
        }

        return null;
    }
}