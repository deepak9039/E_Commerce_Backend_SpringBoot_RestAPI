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

    // upload dir injected from application.properties (default category_img)
    @Value("${app.upload.dir:category_img}")
    private String uploadDir;

    @Value("${app.upload.product:static/image/product}")
    private String uploadProductDir;

    // Category Urls
    // Accept multipart/form-data: a JSON part named "category" and optional file part named "image"
    @PostMapping(value = "/admin/createCategory", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String category(
            @RequestPart("category") Category category,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        // Ensure upload directory exists
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);

        // If an image file is provided, save it and set categoryImage to saved filename
        if (image != null && !image.isEmpty()) {
            String original = StringUtils.cleanPath(image.getOriginalFilename());
            String filename = System.currentTimeMillis() + "_" + original;
            Path target = uploadPath.resolve(filename);
            Files.copy(image.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            category.setCategoryImage(filename);
        } else {
            // fallback: if client sent image filename inside JSON, use it; otherwise default
            String imageUrl = (category.getCategoryImage() != null && !category.getCategoryImage().isBlank())
                    ? category.getCategoryImage() : "default.png";
            category.setCategoryImage(imageUrl);
        }

        Boolean exists = categoryService.existsByCategoryName(category.getCategoryName());
        if (exists) {
            return "Category name already exists";
        }

        Category savedCategory = categoryService.createCategory(category);
        if (Objects.isNull(savedCategory)) {
            return "Something went wrong!";
        }

        return "Category Successfully Saved !!";
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
    public String updateCategory(
            @RequestPart("category") Category category,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        // Fetch existing category
        Category existing = categoryService.findByCategoryId(category.getCategoryId());
        if (existing == null) {
            return "No Category Found With This Category Id.";
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

        categoryService.updateCategory(category);

        return "Category Successfully Updated !!";
    }


    @GetMapping("/getAllCategory")
    public List<Category> getAllCategory() {
        return categoryService.getAllCategory();
    }


    //Product Urls
//    @PostMapping("createProduct")
//    public String createProduct(@RequestBody Product product, HttpSession session) {
//
//        Category existCategory = categoryService.findByCategoryName(product.getCategoryName());
//
//        if (existCategory == null) {
//            return "Provided Category Name Is Invalid : categoryName!";
//        }
//        productService.createproduct(product);
//        return "Product Successfully Saved !!";
//    }
    @PostMapping(value = "/admin/createProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String createProduct(
            @RequestPart("product") Product product,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        // Create product upload directory: static/image/product
        Path uploadPath = Paths.get(uploadProductDir).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);

        if (image != null && !image.isEmpty()) {
            String original = StringUtils.cleanPath(image.getOriginalFilename());
            String filename = System.currentTimeMillis() + "_" + original;
            Path target = uploadPath.resolve(filename);
            Files.copy(image.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            product.setProductImageUrl(filename);
        } else {
            String imageUrl = (product.getProductImageUrl() != null && !product.getProductImageUrl().isBlank())
                    ? product.getProductImageUrl() : "default.png";
            product.setProductImageUrl(imageUrl);
        }

        Boolean exists = productService.existsByProductName(product.getProductName());
        if (exists) {
            return "Product name already exists!";
        }

        Product savedProduct = productService.createproduct(product);
        if (Objects.isNull(savedProduct)) {
            return "Something went wrong!";
        }

        return "Product Successfully Saved !!";
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


//    @PostMapping("updateProduct")
//    public String updateProduct(@RequestBody Product product, HttpSession session) {
//        Product getProductById = productService.findByProductId(product.getProductId());
//        if (getProductById == null) {
//            return "No Product Fount With This Product Id.";
//        }
//        productService.updateProduct(product);
//        return "Product Successfully Updated !!";
//    }

    @PostMapping(value = "/admin/updateProduct", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String updateProduct(
            @RequestPart("product") Product product,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

        // Fetch existing product
        Product existing = productService.findByProductId(product.getProductId());
        if (existing == null) {
            return "No Product Found With This Product Id.";
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

        productService.updateProduct(product);

        return "Product Successfully Updated !!";
    }


}
