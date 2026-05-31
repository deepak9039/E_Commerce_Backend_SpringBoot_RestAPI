package com.store.e_commerce_app.dto;

public class DiscountRequest {
    private Integer minPercent;
    private Integer page;
    private Integer pageSize;

    public DiscountRequest() {}

    public Integer getMinPercent() {
        return minPercent;
    }

    public void setMinPercent(Integer minPercent) {
        this.minPercent = minPercent;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}

