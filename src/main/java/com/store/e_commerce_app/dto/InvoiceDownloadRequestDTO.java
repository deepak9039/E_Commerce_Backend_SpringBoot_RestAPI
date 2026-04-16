package com.store.e_commerce_app.dto;

public class InvoiceDownloadRequestDTO {

    private Long userId;
    private String orderId;

    public Long getUserId() {
        return userId;
    }
    public  void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
