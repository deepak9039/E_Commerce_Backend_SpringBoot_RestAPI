package com.store.e_commerce_app.dto;

public class CategorySalesDTO {

    private String categoryName;
    private Long totalSales;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Long getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Long totalSales) {
        this.totalSales = totalSales;
    }

    public CategorySalesDTO(String categoryName, Long totalSales) {
        this.categoryName = categoryName;
        this.totalSales = totalSales;
    }

}
