package com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo;

public class SelectOrderItemVO {
    private String sku;
    private Integer quantity;
    private String productName;

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
