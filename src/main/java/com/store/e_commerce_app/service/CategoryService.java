package com.store.e_commerce_app.service;

import com.store.e_commerce_app.dto.CategorySalesDTO;
import com.store.e_commerce_app.entities.Category;
import com.store.e_commerce_app.repositories.CategoryRepository;
import com.store.e_commerce_app.repositories.ProductOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository  categoryRepository;

    @Autowired
    private ProductOrderRepository productOrderRepository;

    public Category createCategory(Category category){
        return categoryRepository.save(category);
    }

    public Category updateCategory(Category category){
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategory(){
        return categoryRepository.findAll();
    }

    public Boolean existsByCategoryName(String categoryName){
        return categoryRepository.existsByCategoryName(categoryName);
    }

    public Category findByCategoryName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName);
    }

    public  Category findByCategoryId(Long categoryId) {
        return categoryRepository.findByCategoryId(categoryId);
    }

    public List<CategorySalesDTO> getCategorySalesData() {
        return productOrderRepository.getCategoryWiseSales();
    }

}
