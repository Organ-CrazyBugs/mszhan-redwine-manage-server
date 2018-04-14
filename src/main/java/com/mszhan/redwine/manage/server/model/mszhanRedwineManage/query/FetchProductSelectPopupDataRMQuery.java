package com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query;

import java.math.BigDecimal;

public class FetchProductSelectPopupDataRMQuery {
    private Integer productId;
    private String sku;
    private String productName;
    private BigDecimal generalGentPrice;
    private BigDecimal gentPrice;
    private BigDecimal wholesalePrice;
    private BigDecimal retailPrice;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getGeneralGentPrice() {
        return generalGentPrice;
    }

    public void setGeneralGentPrice(BigDecimal generalGentPrice) {
        this.generalGentPrice = generalGentPrice;
    }

    public BigDecimal getGentPrice() {
        return gentPrice;
    }

    public void setGentPrice(BigDecimal gentPrice) {
        this.gentPrice = gentPrice;
    }

    public BigDecimal getWholesalePrice() {
        return wholesalePrice;
    }

    public void setWholesalePrice(BigDecimal wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }
}
