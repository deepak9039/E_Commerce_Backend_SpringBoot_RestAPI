package com.store.e_commerce_app.repositories;

import com.store.e_commerce_app.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CategoryRepository extends JpaRepository<Category, Long> {
    public Boolean existsByCategoryName(String categoryName);

    Category findByCategoryName(String categoryName);

    Category findByCategoryId(Long categoryId);

}
