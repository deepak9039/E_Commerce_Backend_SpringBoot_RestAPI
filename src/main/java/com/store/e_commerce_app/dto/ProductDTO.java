package com.store.e_commerce_app.dto;

import com.store.e_commerce_app.entities.Product;

public class ProductDTO {
    private Long productId;
    private String productName;
    private String productDescription;
    private String brandDetails;
    private String aboutProduct;
    private String categoryName;
    private Double productPrice;
    private Integer discount;
    private Double discountPrice;
    private int stockQuantity;
    private String productImageUrl;
    private Boolean isSponsored;

    public ProductDTO() {}

    public static ProductDTO from(Product p) {
        if (p == null) return null;
        ProductDTO dto = new ProductDTO();
        dto.productId = p.getProductId();
        dto.productName = p.getProductName();
        dto.productDescription = p.getProductDescription();
        dto.brandDetails = p.getBrandDetails();
        dto.aboutProduct = p.getAboutProduct();
        dto.categoryName = p.getCategoryName();
        dto.productPrice = p.getProductPrice();
        dto.discount = p.getDiscount();
        dto.discountPrice = p.getDiscountPrice();
        dto.stockQuantity = p.getStockQuantity();
        dto.productImageUrl = p.getProductImageUrl();
        dto.isSponsored = p.getIsSponsored();
        return dto;
    }

    // getters and setters
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getProductDescription() { return productDescription; }
    public void setProductDescription(String productDescription) { this.productDescription = productDescription; }
    public String getBrandDetails() { return brandDetails; }
    public void setBrandDetails(String brandDetails) { this.brandDetails = brandDetails; }
    public String getAboutProduct() { return aboutProduct; }
    public void setAboutProduct(String aboutProduct) { this.aboutProduct = aboutProduct; }
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    public Double getProductPrice() { return productPrice; }
    public void setProductPrice(Double productPrice) { this.productPrice = productPrice; }
    public Integer getDiscount() { return discount; }
    public void setDiscount(Integer discount) { this.discount = discount; }
    public Double getDiscountPrice() { return discountPrice; }
    public void setDiscountPrice(Double discountPrice) { this.discountPrice = discountPrice; }
    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    public String getProductImageUrl() { return productImageUrl; }
    public void setProductImageUrl(String productImageUrl) { this.productImageUrl = productImageUrl; }
    public Boolean getIsSponsored() { return isSponsored; }
    public void setIsSponsored(Boolean isSponsored) { this.isSponsored = isSponsored; }
}

