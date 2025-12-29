package com.store.e_commerce_app.controllers;

import com.store.e_commerce_app.entities.Category;
import com.store.e_commerce_app.entities.Product;
import com.store.e_commerce_app.service.CategoryService;
import com.store.e_commerce_app.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Objects;
import java.util.Map;
import org.springframework.http.MediaType;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.util.StringUtils;

@RestController
//@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService  productService;

    // upload dir injected from application.properties (default to resources/static/category_img)
    @Value("${app.upload.dir:src/main/resources/static/category_img}")
    private String uploadDir;

    @Value("${app.upload.product:src/main/resources/static/product_img}")
    private String uploadProductDir;

    // Category Urls
    // Accept multipart/form-data: a JSON part named "category" and optional file part named "image"
    @PostMapping(value = "/admin/createCategory", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> category(
            @RequestPart("category") Category category,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        // Ensure upload directory exists under project resources/static
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);

        // If an image file is provided, save it and set categoryImage to saved filename (only filename)
        if (image != null && !image.isEmpty()) {
            String original = StringUtils.cleanPath(image.getOriginalFilename());
            String filename = System.currentTimeMillis() + "_" + original;
            Path target = uploadPath.resolve(filename);
            Files.copy(image.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            // store only filename in DB
            category.setCategoryImage(filename);
        } else {
            // fallback: if client sent image filename inside JSON, use it; otherwise default
            String imageUrl = (category.getCategoryImage() != null && !category.getCategoryImage().isBlank())
                    ? category.getCategoryImage() : "default.png";
            category.setCategoryImage(imageUrl);
        }

        Boolean exists = categoryService.existsByCategoryName(category.getCategoryName());
        if (exists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Category name already exists");
        }

        Category savedCategory = categoryService.createCategory(category);
        if (Objects.isNull(savedCategory)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong!");
        }

        // Return success message + saved entity
        return ResponseEntity.ok(Map.of("message", "Category created successfully", "category", savedCategory));
    }

    @PostMapping("getCategoryById")
    public Object getCategoryById(@RequestBody Category category, HttpSession session) {
        Category result = categoryService.findByCategoryId(category.getCategoryId());
        if (result == null) {
            return "no category found";
        }
        return result;
    }

    @PostMapping(value = "/admin/updateCategory", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateCategory(
            @RequestPart("category") Category category,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        // Fetch existing category
        Category existing = categoryService.findByCategoryId(category.getCategoryId());
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Category Found With This Category Id.");
        }

        // Ensure upload directory exists
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);

        // Handle image update
        if (image != null && !image.isEmpty()) {

            // Delete old image if exists
            if (existing.getCategoryImage() != null) {
                Path oldImgPath = uploadPath.resolve(existing.getCategoryImage());
                Files.deleteIfExists(oldImgPath);
            }

            String original = StringUtils.cleanPath(image.getOriginalFilename());
            String filename = System.currentTimeMillis() + "_" + original;
            Path target = uploadPath.resolve(filename);
            Files.copy(image.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            category.setCategoryImage(filename);
        } else {
            // Keep old image if none uploaded
            category.setCategoryImage(existing.getCategoryImage());
        }

        // Keep fields that shouldn't be overwritten (optional)
//        category.setCreatedAt(existing.getCreatedAt()); // if you track timestamps
        category.setCategoryId(existing.getCategoryId());
        category.setCategoryId(existing.getCategoryId());

        Category updated = categoryService.updateCategory(category);

        // Return success message + updated entity
        return ResponseEntity.ok(Map.of("message", "Category updated successfully", "category", updated));
    }


    @GetMapping("/getAllCategory")
    public List<Category> getAllCategory() {
        return categoryService.getAllCategory();
    }


    //Product Urls
    @PostMapping(value = "/admin/createProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @RequestPart("product") Product product,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        // Create product upload directory under resources/static/product_img
        Path uploadPath = Paths.get(uploadProductDir).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);

        if (image != null && !image.isEmpty()) {
            String original = StringUtils.cleanPath(image.getOriginalFilename());
            String filename = System.currentTimeMillis() + "_" + original;
            Path target = uploadPath.resolve(filename);
            Files.copy(image.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            // store only filename in DB
            product.setProductImageUrl(filename);
        } else {
            String imageUrl = (product.getProductImageUrl() != null && !product.getProductImageUrl().isBlank())
                    ? product.getProductImageUrl() : "default.png";
            product.setProductImageUrl(imageUrl);
        }

        Boolean exists = productService.existsByProductName(product.getProductName());
        if (exists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Product name already exists!");
        }

        Product savedProduct = productService.createproduct(product);
        if (Objects.isNull(savedProduct)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong!");
        }

        // Return success message + saved product
        return ResponseEntity.ok(Map.of("message", "Product created successfully", "product", savedProduct));
    }

    @GetMapping("/findAllProducts")
    public List<Product> findAllProducts() {
        return productService.findAllProducts();
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {

        Product result = productService.findByProductId(id);
        System.out.println("result = " + result);

        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No product found");
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/admin/updateProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(
            @RequestPart("product") Product product,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        // Fetch existing product
        Product existing = productService.findByProductId(product.getProductId());
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Product Found With This Product Id.");
        }

        // Create upload directory
        Path uploadPath = Paths.get(uploadProductDir).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);

        // Handle image update
        if (image != null && !image.isEmpty()) {

            // Delete old image if exists
            if (existing.getProductImageUrl() != null) {
                Path oldImgPath = uploadPath.resolve(existing.getProductImageUrl());
                Files.deleteIfExists(oldImgPath);
            }

            String originalName = StringUtils.cleanPath(image.getOriginalFilename());
            String fileName = System.currentTimeMillis() + "_" + originalName;

            Path targetLocation = uploadPath.resolve(fileName);
            Files.copy(image.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            product.setProductImageUrl(fileName);
        } else {
            // Keep old image if none uploaded
            product.setProductImageUrl(existing.getProductImageUrl());
        }

        // Keep fields not coming from frontend
//        product.setCreatedAt(existing.getCreatedAt());  // if you track timestamps
        product.setProductId(existing.getProductId());

        Product updated = productService.updateProduct(product);

        // Return success message + updated product
        return ResponseEntity.ok(Map.of("message", "Product updated successfully", "product", updated));
    }

    @PostMapping("findProductsByCategoryName" )
    public ResponseEntity<?> findProductsByCategoryName(@RequestBody Category category, HttpSession session) {
        List<Product> products = productService.findProductsByCategoryName(category.getCategoryName());
        return ResponseEntity.ok(Map.of(
                "message", "Success",
                "products", products
        ));
    }

}
