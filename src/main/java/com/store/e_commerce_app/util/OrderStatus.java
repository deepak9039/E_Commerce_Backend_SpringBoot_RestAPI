package com.store.e_commerce_app.util;

public enum OrderStatus {
    IN_PROGRESS(1,"In Progress"),
    ORDER_RECE(2,"Order Received"),
    PRODUCT_PACK(3,"Product Packaging"),
    OUT_FOR_DEL(4,"Out for Delivery"),
    DELIVERED(5,"Delivered");

    private Integer id;

    private String name;

    private OrderStatus(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
