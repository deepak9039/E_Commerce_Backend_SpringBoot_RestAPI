package com.store.e_commerce_app.dto;

public class TotalRevenueDTO {
    private Double totalRevenue;

    public TotalRevenueDTO(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
