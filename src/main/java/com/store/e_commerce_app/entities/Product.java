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

    @Column(name = "variant_name")
    private String variantName;

    @Column(name = "color")
    private String color;

    @Column(name = "size")
    private String size;

    @Column(name = "material")
    private String material;

    @Column(name = "style")
    private String style;

    @Column(name = "weight")
    private String weight;

    @Column(name = "dimensions")
    private String dimensions;

    @Column(name = "country_of_origin")
    private String countryOfOrigin;

    @Column(name = "warranty")
    private String warranty;

    @Column(name = "manufacturer")
    private String manufacturer;

    @Column(name = "fabrics")
    private String fabrics;

    @Column(name = "occasions")
    private String occasions;

    @Column(name = "size_and_fit")
    private String sizeAndFit;

    @Column(name = "material_and_care")
    private String materialAndCare;

    @Column(name = "is_sponsored", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isSponsored = false;

    // owner admin who created/owns this product
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private UserDlts owner;

    public UserDlts getOwner() {
        return owner;
    }

    public void setOwner(UserDlts owner) {
        this.owner = owner;
    }

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

    public String getVariantName() {
        return variantName;
    }

    public void setVariantName(String variantName) {
        this.variantName = variantName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getFabrics() {
        return fabrics;
    }

    public void setFabrics(String fabrics) {
        this.fabrics = fabrics;
    }

    public String getOccasions() {
        return occasions;
    }

    public void setOccasions(String occasions) {
        this.occasions = occasions;
    }

    public String getSizeAndFit() {
        return sizeAndFit;
    }

    public void setSizeAndFit(String sizeAndFit) {
        this.sizeAndFit = sizeAndFit;
    }

    public String getMaterialAndCare() {
        return materialAndCare;
    }

    public void setMaterialAndCare(String materialAndCare) {
        this.materialAndCare = materialAndCare;
    }

    public Boolean getIsSponsored() {
        return isSponsored;
    }

    public void setIsSponsored(Boolean isSponsored) {
        this.isSponsored = isSponsored;
    }

    public Product() {
    }

    public Product(Long productId, String productName, String productDescription, String brandDetails, String aboutProduct, String categoryName, Double productPrice, Integer discount, Double discountPrice, int stockQuantity, String productImageUrl, String variantName, String color, String size, String material, String style, String weight, String dimensions, String countryOfOrigin, String warranty, String manufacturer, String fabrics, String occasions, String sizeAndFit, String materialAndCare, Boolean isSponsored, UserDlts owner) {
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
        this.variantName = variantName;
        this.color = color;
        this.size = size;
        this.material = material;
        this.style = style;
        this.weight = weight;
        this.dimensions = dimensions;
        this.countryOfOrigin = countryOfOrigin;
        this.warranty = warranty;
        this.manufacturer = manufacturer;
        this.fabrics = fabrics;
        this.occasions = occasions;
        this.sizeAndFit = sizeAndFit;
        this.materialAndCare = materialAndCare;
        this.isSponsored = isSponsored;
        this.owner = owner;
    }
}
