package com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo;

import javax.persistence.Transient;
import java.util.Date;

public class FetchInventoryVO {
    private Integer id;
    private String sku;
    private String warehouseName;
    private String productName;
    private String brandName;
    private String wineType;
    private Date updateDate;
    private Integer quantity;
    private String unit;
    @Transient
    private String quantityDes;

    public String getQuantityDes() {
        return quantityDes;
    }

    public void setQuantityDes(String quantityDes) {
        this.quantityDes = quantityDes;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getWineType() {
        return wineType;
    }

    public void setWineType(String wineType) {
        this.wineType = wineType;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
