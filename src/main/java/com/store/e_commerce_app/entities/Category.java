package com.store.e_commerce_app.entities;
import jakarta.persistence.*;

@Entity
@Table(name="Category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryId")
    private Long categoryId;

    @Column(name = "categoryName")
    private String categoryName;

    @Column(name = "categoryDescription")
    private String categoryDescription;

    @Column(name = "categoryImage")
    private String categoryImage;

    @Column(name = "isActive")
    private Boolean  isActive;

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Category() {
        // Hibernate requires this
    }

    public Category(Long categoryId, String categoryName, String categoryDescription, String categoryImage, Boolean isActive) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.categoryImage = categoryImage;
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", categoryDescription='" + categoryDescription + '\'' +
                ", categoryImage='" + categoryImage + '\'' +
                '}';
    }
}
