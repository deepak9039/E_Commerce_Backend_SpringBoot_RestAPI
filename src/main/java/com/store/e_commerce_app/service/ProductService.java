package com.store.e_commerce_app.service;

import com.store.e_commerce_app.dto.TopProductSalesDTO;
import com.store.e_commerce_app.entities.Product;
import com.store.e_commerce_app.repositories.CategoryRepository;
import com.store.e_commerce_app.repositories.ProductOrderRepository;
import com.store.e_commerce_app.repositories.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public List<Product> findProductsByCategoryName(String categoryName){
        return productRepository.findBycategoryName(categoryName);
    }

    public List<Product> saveAll(List<Product> products) {
        return productRepository.saveAll(products);
    }

    public Page<Product> findAllProductsWithPage(int pageNumber, int pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return productRepository.findAll(pageable);
    }

    public List<Product> searchProducts(String query) {
        return productRepository.findByProductNameContainingIgnoreCaseOrCategoryNameContainingIgnoreCase(query, query);
    }

    public List<TopProductSalesDTO> getTopSellingProducts() {
        return productOrderRepository.getTopSellingProducts();
    }


}
