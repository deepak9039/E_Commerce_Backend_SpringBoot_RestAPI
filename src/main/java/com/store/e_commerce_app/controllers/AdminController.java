package com.store.e_commerce_app.controllers;

import com.store.e_commerce_app.config.CustomUser;
import com.store.e_commerce_app.dto.*;
import com.store.e_commerce_app.entities.Category;
import com.store.e_commerce_app.entities.Product;
import com.store.e_commerce_app.entities.ProductOrder;
import com.store.e_commerce_app.entities.UserDlts;
import com.store.e_commerce_app.repositories.ProductOrderRepository;
import com.store.e_commerce_app.repositories.UserDltsRepository;
import com.store.e_commerce_app.service.CategoryService;
import com.store.e_commerce_app.service.ProductOrderService;
import com.store.e_commerce_app.service.ProductService;
import com.store.e_commerce_app.service.UserDltsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
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
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5174")
@RestController
//@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService  productService;

    @Autowired
    ProductOrderRepository productOrderRepository;

    @Autowired
    UserDltsRepository userDltsRepository;

    @Autowired
    private ProductOrderService productOrderService;

    @Autowired
    private UserDltsService userDltsService;

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
            @RequestPart(value = "image", required = false) MultipartFile image,
            Authentication authentication) throws IOException {

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

        // Compute discountPrice for the product on create (use double division to avoid integer truncation)
        if (product.getProductPrice() != null && product.getDiscount() != null) {
            double price = product.getProductPrice();
            double pct = product.getDiscount() / 100.0;
            double discountAmount = price * pct;
            double discountPrice = price - discountAmount;
            product.setDiscountPrice(discountPrice);
        }

        // Set owner based on authenticated user
        if (authentication != null && authentication.getPrincipal() instanceof CustomUser) {
            CustomUser cu = (CustomUser) authentication.getPrincipal();
            Long adminId = cu.getUserId();
            UserDlts owner = userDltsService.findByUserId(adminId);
            product.setOwner(owner);
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

    // Return products owned by the authenticated admin (super admin can see all)
    @PostMapping("/admin/myProducts")
    public ResponseEntity<?> myProducts(@RequestBody com.store.e_commerce_app.dto.PageRequestDTO pageRequest, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","Unauthorized"));
        }
        CustomUser cu = (CustomUser) authentication.getPrincipal();
        String role = cu.getRole();

        int page = pageRequest.getPage() < 0 ? 0 : pageRequest.getPage();
        int pageSize = pageRequest.getPageSize() <= 0 ? 50 : pageRequest.getPageSize();

        if ("SUPER_ADMIN".equals(role)) {
            Page<Product> pageResult = productService.findAllProductsWithPageSorted(page, pageSize);
            return ResponseEntity.ok(Map.of(
                    "status","Success",
                    "products", pageResult.getContent(),
                    "currentPage", pageResult.getNumber(),
                    "totalPages", pageResult.getTotalPages(),
                    "totalItems", pageResult.getTotalElements()
            ));
        } else {
            Page<Product> pageResult = productService.findProductsByOwnerPaged(cu.getUserId(), page, pageSize);
            return ResponseEntity.ok(Map.of(
                    "status","Success",
                    "products", pageResult.getContent(),
                    "currentPage", pageResult.getNumber(),
                    "totalPages", pageResult.getTotalPages(),
                    "totalItems", pageResult.getTotalElements()
            ));
        }
    }

    // Endpoint to change product status - only SUPER_ADMIN allowed
    @PostMapping("/admin/changeProductStatus")
    public ResponseEntity<?> changeProductStatus(@RequestBody Map<String, Object> body, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","Unauthorized"));
        }
        CustomUser cu = (CustomUser) authentication.getPrincipal();
        if (!"SUPER_ADMIN".equals(cu.getRole())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message","Only super admin can change product status"));
        }
        if (!body.containsKey("productId") || !body.containsKey("status")) {
            return ResponseEntity.badRequest().body(Map.of("message","productId and status are required"));
        }
        Long productId = Long.valueOf(String.valueOf(body.get("productId")));
        String status = String.valueOf(body.get("status"));
        Product prod = productService.findByProductId(productId);
        if (prod == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","Product not found"));
        }
        // set status - existing Product entity doesn't have status field, this is a business attribute we can store in ProductOrder or elsewhere
        // For now we'll set isSponsored as a proxy for status change (you can replace with a dedicated status field)
        prod.setIsSponsored("ACTIVE".equalsIgnoreCase(status));
        productService.updateProduct(prod);
        return ResponseEntity.ok(Map.of("message","Product status updated","product", prod));
    }


    @PostMapping("createBulkProducts")
    public ResponseEntity<?> createProductsWithoutImage(
            @RequestBody List<Product> products) {

        if (products == null || products.isEmpty()) {
            return ResponseEntity.badRequest().body("Product list cannot be empty");
        }

        if (products.size() > 50) {
            return ResponseEntity.badRequest()
                    .body("You can create maximum 50 products at a time");
        }

        // Check duplicate names (DB level)
        for (Product product : products) {
            if (productService.existsByProductName(product.getProductName())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Product already exists: " + product.getProductName());
            }
            // set default image
            product.setProductImageUrl("default.png");
        }

        List<Product> savedProducts = productService.saveAll(products);

        return ResponseEntity.ok(Map.of(
                "message", "Products created successfully",
                "count", savedProducts.size(),
                "products", savedProducts
        ));
    }


    @PostMapping("/admin/findAllProducts")
    public ResponseEntity<?> findAllProducts(@RequestBody PageRequest pageRequest) {
        if (pageRequest.getPage() < 0 || pageRequest.getPageSize() <= 0) {
            return ResponseEntity.badRequest().body("Invalid page number or size");
        }
        var pageResult = productService.findAllProductsWithPage(pageRequest.getPage(), pageRequest.getPageSize());
        return ResponseEntity.ok(Map.of(
                "message", "Products fetched successfully",
                "products", pageResult.getContent(),
                "page", pageRequest.getPage(),
                "pageSize", pageRequest.getPageSize(),
                "totalPages", pageResult.getTotalPages(),
                "totalElements", pageResult.getTotalElements()
        ));

        //        return productService.findAllProducts();
    }

    @PostMapping("/findAllProducts")
    public ResponseEntity<?> findAllProductsAdmin(@RequestBody PageRequest pageRequest) {
        if (pageRequest.getPage() < 0 || pageRequest.getPageSize() <= 0) {
            return ResponseEntity.badRequest().body("Invalid page number or size");
        }
        var pageResult = productService.findAllProductsWithPage(pageRequest.getPage(), pageRequest.getPageSize());
        return ResponseEntity.ok(Map.of(
                "message", "Products fetched successfully",
                "products", pageResult.getContent(),
                "page", pageRequest.getPage(),
                "pageSize", pageRequest.getPageSize(),
                "totalPages", pageResult.getTotalPages(),
                "totalElements", pageResult.getTotalElements()
        ));

        //        return productService.findAllProducts();
    }


    @PostMapping("findAllSponsoredProducts")
    public ResponseEntity<?> findAllSponsoredProducts() {
        List<Product> products = productService.findAllSponsoredProducts();
        return ResponseEntity.ok(Map.of(
                "message", "Success",
                "products", products
        ));
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
            @RequestPart(value = "image", required = false) MultipartFile image,
            Authentication authentication) throws IOException {

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

        // Compute discountPrice for update: prefer incoming values, fallback to existing
        Double priceToUse = product.getProductPrice() != null ? product.getProductPrice() : existing.getProductPrice();
        Integer discountToUse = product.getDiscount() != null ? product.getDiscount() : existing.getDiscount();

        double discountPrice = 0.0;
        if (priceToUse != null && discountToUse != null) {
            double pct = discountToUse / 100.0;
            double discountAmount = priceToUse * pct;
            discountPrice = priceToUse - discountAmount;
        }

        // Ensure the product object we save contains the computed discount and discountPrice
        if (product.getDiscount() == null) {
            product.setDiscount(existing.getDiscount());
        }
        product.setDiscountPrice(discountPrice);

        // Set owner based on authenticated user
        if (authentication != null && authentication.getPrincipal() instanceof CustomUser) {
            CustomUser cu = (CustomUser) authentication.getPrincipal();
            Long adminId = cu.getUserId();
            UserDlts owner = userDltsService.findByUserId(adminId);
            product.setOwner(owner);
        }

        Product updated = productService.updateProduct(product);

        // Return success message + updated product
        return ResponseEntity.ok(Map.of("message", "Success", "product", updated));
    }

    @PostMapping("findProductsByCategoryName")
    public ResponseEntity<?> findProductsByCategoryName(@RequestBody com.store.e_commerce_app.dto.FindProductsByCategoryRequest request) {
//        if (request.getCategoryName() == null || request.getCategoryName().isBlank()) {
//            return ResponseEntity.badRequest().body(Map.of("status", "Failure", "message", "categoryName is required"));
//        }
//        int page = request.getPage() < 0 ? 0 : request.getPage();
//        int pageSize = request.getPageSize() <= 0 ? 50 : request.getPageSize();

        var pageResult = productService.findProductsByCategoryName(request.getCategoryName(),request.getPage(), request.getPageSize());


//        List<Product> products = productService.findProductsByCategoryName(request.getCategoryName(), page, pageSize);
        return ResponseEntity.ok(Map.of(
                "status", "Success",
                "products", pageResult.getContent(),
                "page", request.getPage(),
                "pageSize", request.getPageSize(),
                "totalPages", pageResult.getTotalPages(),
                "totalElements", pageResult.getTotalElements()
        ));
    }

    @PostMapping("ordersCount")
    public ResponseEntity<?> getOrdersCount() {
        // Dummy implementation, replace with actual logic
        long ordersCount = productOrderRepository.count();
//        int ordersCount = 42; // Example static count
        return ResponseEntity.ok(Map.of(
                "message", "Success",
                "ordersCount", ordersCount
        ));
    }

    @PostMapping("productsCount")
    public ResponseEntity<?> getProductsCount() {
        long productsCount = productService.findAllProducts().size();
        return ResponseEntity.ok(Map.of(
                "message", "Success",
                "productsCount", productsCount
        ));
    }

    @PostMapping("totalRevenue")
    public ResponseEntity<?> getTotalRevenue() {
        Double totalRevenue = productOrderService.totalRevenue().getTotalRevenue();
        return ResponseEntity.ok(Map.of(
                "message", "Success",
                "totalRevenue", totalRevenue != null ? totalRevenue : 0.0
        ));
    }

    @PostMapping("usersCount")
    public ResponseEntity<?> getUsersCount() {
//        long usersCount = 0; // Replace with actual user count retrieval logic
        long usersCount = userDltsRepository.count();
        return ResponseEntity.ok(Map.of(
                "message", "Success",
                "usersCount", usersCount
        ));
    }

    @PostMapping("/categorySales")
    public ResponseEntity<?> getCategoryWiseSales() {

        List<CategorySalesDTO> sales = categoryService.getCategorySalesData();

        return ResponseEntity.ok(
                Map.of(
                        "message", "Success",
                        "data", sales
                )
        );
    }

    @PostMapping("topSellingProducts")
    public ResponseEntity<?> getTopSellingProducts() {
    List<TopProductSalesDTO> topProductSale = productService.getTopSellingProducts();
    return ResponseEntity.ok(Map.of(
            "message", "Success",
            "products", topProductSale
    ));
    }

    @PostMapping("orderDesc")
    public ResponseEntity<?> getOrderDesc() {
        List<ProductOrder> orders = productOrderService.getLatestOrders();
        return ResponseEntity.ok(Map.of(
                "message", "Success",
                "orders", orders
        ));
    }

    @GetMapping("/sales-overview")
    public ResponseEntity<?> getSalesOverview(){

        List<SalesOverviewDTO> data = productOrderService.getWeeklySalesOverview();

        return ResponseEntity.ok(Map.of(
                "status","Success",
                "data", data
        ));
    }

    // helper: check if authenticated principal is super admin (accept both ROLE_SUPER_ADMIN and SUPER_ADMIN)
    private boolean isSuperAdmin(CustomUser cu, Authentication authentication) {
        if (cu == null) return false;
        String role = cu.getRole();
        if (role != null && (role.equalsIgnoreCase("SUPER_ADMIN") || role.equalsIgnoreCase("ROLE_SUPER_ADMIN"))) {
            return true;
        }
        if (authentication != null) {
            return authentication.getAuthorities().stream()
                    .map(a -> a.getAuthority())
                    .anyMatch(s -> s != null && s.toUpperCase().contains("SUPER_ADMIN"));
        }
        return false;
    }

    @PostMapping("/admin/getOrdersForAdmin")
    public ResponseEntity<?> getOrdersForAdmin(@RequestBody com.store.e_commerce_app.dto.PageRequest pageRequest, Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","Unauthorized"));
        }
        CustomUser cu = (CustomUser) authentication.getPrincipal();

        int page = Math.max(0, pageRequest.getPage());
        int pageSize = pageRequest.getPageSize() <= 0 ? 50 : pageRequest.getPageSize();

        // If super admin, return all orders paged
        if (isSuperAdmin(cu, authentication)) {
            var pageResult = productOrderService.getAllOrders(page, pageSize);
            return ResponseEntity.ok(Map.of(
                    "message", "Orders fetched successfully",
                    "orders", pageResult.getContent(),
                    "page", page,
                    "pageSize", pageSize,
                    "totalPages", pageResult.getTotalPages(),
                    "totalElements", pageResult.getTotalElements()
            ));
        }

        // For normal admins, return orders for products owned by this admin
        var pageResult = productOrderRepository.findByProductOwnerId(cu.getUserId(), org.springframework.data.domain.PageRequest.of(page, pageSize));
        return ResponseEntity.ok(Map.of(
                "message", "Orders fetched successfully",
                "orders", pageResult.getContent(),
                "page", page,
                "pageSize", pageSize,
                "totalPages", pageResult.getTotalPages(),
                "totalElements", pageResult.getTotalElements()
        ));
    }

    @PostMapping("/admin/getOrdersForAdminList")
    public ResponseEntity<?> getOrdersForAdminList(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message","Unauthorized"));
        }
        CustomUser cu = (CustomUser) authentication.getPrincipal();

        if (isSuperAdmin(cu, authentication)) {
            List<ProductOrder> all = productOrderRepository.findAll();
            return ResponseEntity.ok(Map.of("message","Success","orders", all));
        }

        List<ProductOrder> orders = productOrderRepository.findByProductOwnerId(cu.getUserId());
        return ResponseEntity.ok(Map.of("message","Success","orders", orders));
    }

    @GetMapping("/admin/auth/me")
    public ResponseEntity<?> authMe(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("authenticated", false));
        }
        CustomUser cu = (CustomUser) authentication.getPrincipal();
        var auths = cu.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return ResponseEntity.ok(Map.of(
                "authenticated", true,
                "userId", cu.getUserId(),
                "username", cu.getUsername(),
                "roleFromDb", cu.getRole(),
                "authorities", auths
        ));
    }

    @PostMapping("/admin/getAllSellers")
    public ResponseEntity<?> getAllSellers() {
        String role = "ROLE_ADMIN";
        List<UserDlts> sellers = userDltsService.getAllSellers(role);
        return ResponseEntity.ok(Map.of("message","Success","sellers", sellers));
    }

    @PostMapping("/admin/getAllUsers")
    public ResponseEntity<?> getAllUsers(){
        String role = "ROLE_USER";
        List<UserDlts> users = userDltsService.getAllSellers(role);
        return ResponseEntity.ok(Map.of("message","Success","users", users));
    }


}
