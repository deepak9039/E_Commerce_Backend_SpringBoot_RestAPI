package com.store.e_commerce_app.dto;

import java.math.BigDecimal;

public class SalesOverviewDTO {

    private String day;
    private Long totalOrders;
    private Double totalSales;

    public SalesOverviewDTO() {}

    public SalesOverviewDTO(String day, Long totalOrders, Double totalSales) {
        this.day = day;
        this.totalOrders = totalOrders;
        this.totalSales = totalSales;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public Double getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Double totalSales) {
        this.totalSales = totalSales;
    }
}