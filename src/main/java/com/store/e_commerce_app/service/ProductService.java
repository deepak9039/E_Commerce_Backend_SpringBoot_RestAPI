package com.store.e_commerce_app.service;

import com.store.e_commerce_app.entities.Product;
import com.store.e_commerce_app.repositories.CategoryRepository;
import com.store.e_commerce_app.repositories.ProductRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Product createproduct(Product product){
        return productRepository.save(product);
    }

    public List<Product> findAllProducts(){
        return productRepository.findAll();
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

}
