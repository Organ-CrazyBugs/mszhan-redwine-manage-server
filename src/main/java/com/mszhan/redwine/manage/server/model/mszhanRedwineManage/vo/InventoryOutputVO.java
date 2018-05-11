package com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo;

/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 13:03 2018/5/11
 */
public class InventoryOutputVO {
    private Integer bottleQty;
    private String inputType;
    private String remark;
    private String sku;
    private Integer warehouseId;

    public Integer getBottleQty() {
        return bottleQty;
    }

    public void setBottleQty(Integer bottleQty) {
        this.bottleQty = bottleQty;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }
}
