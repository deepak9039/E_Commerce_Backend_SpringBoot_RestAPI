package com.store.e_commerce_app.dto;

public class TopProductSalesDTO {
    Long productId;
    String productName;
    Long quantity;

    Double totalAmount;

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

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long totalSales) {
        this.quantity = totalSales;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public TopProductSalesDTO(Long productId, String productName, Long quantity, Double totalAmount) {
        this.productId = productId;
        this.productName = productName;
        this.quantity =  quantity;
        this.totalAmount = totalAmount;
    }

}
