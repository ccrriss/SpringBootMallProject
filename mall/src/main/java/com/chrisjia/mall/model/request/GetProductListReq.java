package com.chrisjia.mall.model.request;

public class GetProductListReq {
    private String keyword;

    private Integer categoryId;

    private String orderby;

    private Integer page = 1;
    private Integer limit = 10;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getOrderby() {
        return orderby;
    }

    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "GetProductListReq{" +
                "keyword='" + keyword + '\'' +
                ", categoryId=" + categoryId +
                ", orderby='" + orderby + '\'' +
                ", page=" + page +
                ", limit=" + limit +
                '}';
    }
}
