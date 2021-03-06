package com.mszhan.redwine.manage.server.model.mszhanRedwineManage;

import java.util.Date;
import javax.persistence.*;

@Table(name = "outbound_history")
public class OutboundHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * (TRANSTER_OUTBOUND调拨出库，SALES_OUTBOUND销售出库，OTHER其他)
     */
    private String type;

    @Column(name = "product_id")
    private Integer productId;

    private String sku;

    private Integer quantity;

    @Column(name = "create_date")
    private Date createDate;

    private Integer creator;

    @Column(name = "creator_name")
    private String creatorName;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "order_item_id")
    private Integer orderItemId;

    @Column(name = "warehouse_id")
    private Integer warehouseId;

    private String remark;

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取(TRANSTER_OUTBOUND调拨出库，SALES_OUTBOUND销售出库，OTHER其他)
     *
     * @return type - (TRANSTER_OUTBOUND调拨出库，SALES_OUTBOUND销售出库，OTHER其他)
     */
    public String getType() {
        return type;
    }

    /**
     * 设置(TRANSTER_OUTBOUND调拨出库，SALES_OUTBOUND销售出库，OTHER其他)
     *
     * @param type (TRANSTER_OUTBOUND调拨出库，SALES_OUTBOUND销售出库，OTHER其他)
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return product_id
     */
    public Integer getProductId() {
        return productId;
    }

    /**
     * @param productId
     */
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    /**
     * @return sku
     */
    public String getSku() {
        return sku;
    }

    /**
     * @param sku
     */
    public void setSku(String sku) {
        this.sku = sku;
    }

    /**
     * @return quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * @param quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * @return create_date
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return creator
     */
    public Integer getCreator() {
        return creator;
    }

    /**
     * @param creator
     */
    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    /**
     * @return creator_name
     */
    public String getCreatorName() {
        return creatorName;
    }

    /**
     * @param creatorName
     */
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    /**
     * @return order_id
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * @return order_item_id
     */
    public Integer getOrderItemId() {
        return orderItemId;
    }

    /**
     * @param orderItemId
     */
    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }
}