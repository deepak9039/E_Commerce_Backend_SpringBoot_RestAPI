package com.store.e_commerce_app.dto;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Map;

public class SearchProductsRequest {

    private String query;
    private Integer page;
    private Integer pageSize;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
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

    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
        if ("query".equals(key) && value instanceof String s) {
            this.query = s;
            return;
        }

        if ("query".equals(key) && value instanceof Map<?, ?> map) {
            Object nestedQuery = map.get("query");
            if (nestedQuery instanceof String s) {
                this.query = s;
            }
            Object nestedPage = map.get("page");
            if (nestedPage instanceof Number n) {
                this.page = n.intValue();
            }
            Object nestedPageSize = map.get("pageSize");
            if (nestedPageSize instanceof Number n) {
                this.pageSize = n.intValue();
            }
        }

        if ("page".equals(key) && value instanceof Number n) {
            this.page = n.intValue();
        }

        if ("pageSize".equals(key) && value instanceof Number n) {
            this.pageSize = n.intValue();
        }
    }

    @JsonIgnore
    public String getNormalizedQuery() {
        return query == null ? "" : query.trim();
    }
}
