package com.store.e_commerce_app.service;

import com.store.e_commerce_app.dto.TopProductSalesDTO;
import com.store.e_commerce_app.entities.Product;
import com.store.e_commerce_app.entities.UserDlts;
import com.store.e_commerce_app.repositories.CategoryRepository;
import com.store.e_commerce_app.repositories.ProductOrderRepository;
import com.store.e_commerce_app.repositories.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductOrderRepository productOrderRepository;

    public Product createproduct(Product product){

        // if discount is not provided, default to 0
        Integer discount = product.getDiscount() != null ? product.getDiscount() : 0;
        product.setDiscount(discount);

        // compute discountPrice from productPrice and discount (prefer server-side calculation)
        Double price = product.getProductPrice();
        double discountPrice = 0.0;
        if (price != null) {
            double pct = discount / 100.0;
            discountPrice = price - (price * pct);
        }
        product.setDiscountPrice(discountPrice);

        return productRepository.save(product);
    }

    // Create product and set its owner (admin who created it)
    public Product createProductWithOwner(Product product, UserDlts owner) {
        product.setOwner(owner);
        return createproduct(product);
    }

    // Return products owned by a particular admin user
    public List<Product> findProductsByOwner(Long ownerId) {
        return productRepository.findByOwner_UserId(ownerId);
    }

    // Return products not owned by a particular admin (optional)
    public List<Product> findProductsNotOwnedBy(Long ownerId) {
        return productRepository.findByOwner_UserIdNot(ownerId);
    }

    public List<Product> findAllProducts(){
        return productRepository.findAll();
    }

    public List<Product> findAllSponsoredProducts(){
        return productRepository.findByIsSponsoredTrue();
    }

    public Product findByProductId(Long productId){
        return productRepository.findByProductId(productId);
    }

    public Product updateProduct(Product product){
        return productRepository.save(product);
    }

    public boolean existsByProductName(String productName){
        return productRepository.existsByProductName(productName);
    }

    public Page<Product> findProductsByCategoryName(String categoryName, int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return productRepository.findBycategoryName(categoryName, pageable);
    }

    public List<Product> saveAll(List<Product> products) {
        return productRepository.saveAll(products);
    }

    public Page<Product> findAllProductsWithPage(int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return productRepository.findAll(pageable);
    }

    public Page<Product> searchProducts(String query, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize); // Adjust page number and size as needed
        return productRepository.findByProductNameContainingIgnoreCaseOrCategoryNameContainingIgnoreCase(query, query, pageable);
    }

    public List<TopProductSalesDTO> getTopSellingProducts() {
        return productOrderRepository.getTopSellingProducts();
    }

    // New method: find products whose discount percentage is >= minPercent
    public List<Product> findProductsByMinDiscount(int minPercent) {
        // repository returns products with discount >= minPercent
        List<Product> products = productRepository.findByDiscountGreaterThanEqual(minPercent);
        // Ensure discountPrice is set for each product (fallback to productPrice when null)
        for (Product p : products) {
            if (p.getDiscountPrice() == null) {
                Double price = p.getProductPrice();
                Integer disc = p.getDiscount() != null ? p.getDiscount() : 0;
                if (price != null) {
                    double pct = disc / 100.0;
                    double dp = price - (price * pct);
                    p.setDiscountPrice(dp);
                } else {
                    p.setDiscountPrice(0.0);
                }
            }
        }
        return products;
    }

    public Page<Product> findProductsByOwnerPaged(Long ownerId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "productId"));
        return productRepository.findByOwner_UserId(ownerId, pageable);
    }

    // If super admin wants paged results across all products
    public Page<Product> findAllProductsWithPageSorted(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "productId"));
        return productRepository.findAll(pageable);
    }

}
