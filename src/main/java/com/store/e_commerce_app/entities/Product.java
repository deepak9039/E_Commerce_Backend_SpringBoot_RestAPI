package com.store.e_commerce_app.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productId")
    private Long productId;

    @Column(name = "productName")
    private String productName;

    @Column(name = "productDescription")
    private String productDescription;

    @Column(name = "brandDetails")
    private String brandDetails;

    @Column(name = "aboutProduct")
    private String aboutProduct;

    @Column(name = "categoryName")
    private String categoryName;

    @Column(name = "productPrice")
    private Double productPrice;

    @Column(name = "discount")
    private Integer discount;

    @Column(name = "discountPrice")
    private Double discountPrice;

    @Column(name = "stockQuantity")
    private int stockQuantity;

    @Column(name = "productImageUrl")
    private String productImageUrl;

    @Column(name = "is_sponsored", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isSponsored = false;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getBrandDetails() {
        return brandDetails;
    }

    public void setBrandDetails(String brandDetails) {
        this.brandDetails = brandDetails;
    }

    public String getAboutProduct() {
        return aboutProduct;
    }

    public void setAboutProduct(String aboutProduct) {
        this.aboutProduct = aboutProduct;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public Boolean getIsSponsored() {
        return isSponsored;
    }

    public void setIsSponsored(Boolean isSponsored) {
        this.isSponsored = isSponsored;
    }

    public Product() {
    }

    public Product(Long productId, String productName, String productDescription, String brandDetails, String aboutProduct, String categoryName, Double productPrice, Integer discount, Double discountPrice, int stockQuantity, String productImageUrl, Boolean isSponsored) {
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.brandDetails = brandDetails;
        this.aboutProduct = aboutProduct;
        this.categoryName = categoryName;
        this.productPrice = productPrice;
        this.discount = discount;
        this.discountPrice = discountPrice;
        this.stockQuantity = stockQuantity;
        this.productImageUrl = productImageUrl;
        this.isSponsored = isSponsored;
    }
}
