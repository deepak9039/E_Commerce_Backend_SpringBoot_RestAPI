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
    private String variantName;
    private String color;
    private String size;
    private String material;
    private String style;
    private String weight;
    private String dimensions;
    private String countryOfOrigin;
    private String warranty;
    private String manufacturer;
    private String fabrics;
    private String occasions;
    private String sizeAndFit;
    private String materialAndCare;
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
        dto.variantName = p.getVariantName();
        dto.color = p.getColor();
        dto.size = p.getSize();
        dto.material = p.getMaterial();
        dto.style = p.getStyle();
        dto.weight = p.getWeight();
        dto.dimensions = p.getDimensions();
        dto.countryOfOrigin = p.getCountryOfOrigin();
        dto.warranty = p.getWarranty();
        dto.manufacturer = p.getManufacturer();
        dto.fabrics = p.getFabrics();
        dto.occasions = p.getOccasions();
        dto.sizeAndFit = p.getSizeAndFit();
        dto.materialAndCare = p.getMaterialAndCare();
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
    public String getVariantName() { return variantName; }
    public void setVariantName(String variantName) { this.variantName = variantName; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }
    public String getStyle() { return style; }
    public void setStyle(String style) { this.style = style; }
    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }
    public String getDimensions() { return dimensions; }
    public void setDimensions(String dimensions) { this.dimensions = dimensions; }
    public String getCountryOfOrigin() { return countryOfOrigin; }
    public void setCountryOfOrigin(String countryOfOrigin) { this.countryOfOrigin = countryOfOrigin; }
    public String getWarranty() { return warranty; }
    public void setWarranty(String warranty) { this.warranty = warranty; }
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public String getFabrics() { return fabrics; }
    public void setFabrics(String fabrics) { this.fabrics = fabrics; }
    public String getOccasions() { return occasions; }
    public void setOccasions(String occasions) { this.occasions = occasions; }
    public String getSizeAndFit() { return sizeAndFit; }
    public void setSizeAndFit(String sizeAndFit) { this.sizeAndFit = sizeAndFit; }
    public String getMaterialAndCare() { return materialAndCare; }
    public void setMaterialAndCare(String materialAndCare) { this.materialAndCare = materialAndCare; }
    public Boolean getIsSponsored() { return isSponsored; }
    public void setIsSponsored(Boolean isSponsored) { this.isSponsored = isSponsored; }
}

