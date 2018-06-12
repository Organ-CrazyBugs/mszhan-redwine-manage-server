package com.mszhan.redwine.manage.server.model.mszhanRedwineManage;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 订单号
     */
    @Column(name = "order_id")
    private String orderId;


    /**
     * sku
     */
    private String sku;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 运费
     */
    @Column(name = "shipping_fee")
    private BigDecimal shippingFee;

    /**
     * 包装费
     */
    @Column(name = "packaging_fee")
    private BigDecimal packagingFee;

    /**
     * 单价
     */
    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    /**
     * 创建人
     */
    private Integer creator;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 更新时间
     */
    @Column(name = "update_date")
    private Date updateDate;

    /**
     * 更新人
     */
    private Integer updator;

    /**
     * 代理id
     */
    @Column(name = "agent_id")
    private Integer agentId;

    /**
     * (GIFT赠品,SALES销售商品)
     */
    private String type;

    @Column(name = "delivery_person_id")
    private Integer deliveryPersonId;

    @Column(name = "delivery_person_name")
    private String deliveryPersonName;

    @Column(name = "warehouse_id")
    private Integer warehouseId;

    @Column(name = "warehouse_name")
    private String warehouseName;

    @Transient
    private String productName;
    @Transient
    private boolean redWine;
    @Transient
    private String quantityDescription;

    public String getQuantityDescription() {
        return quantityDescription;
    }

    public void setQuantityDescription(String quantityDescription) {
        this.quantityDescription = quantityDescription;
    }

    public boolean isRedWine() {
        return redWine;
    }

    public void setRedWine(boolean redWine) {
        this.redWine = redWine;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal fetchAmountTotal(){
        if (this.shippingFee == null) {
            this.shippingFee = BigDecimal.ZERO;
        }
        return this.unitPrice.multiply(new BigDecimal(this.quantity)).add(this.shippingFee).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public Integer getDeliveryPersonId() {
        return deliveryPersonId;
    }

    public void setDeliveryPersonId(Integer deliveryPersonId) {
        this.deliveryPersonId = deliveryPersonId;
    }

    public String getDeliveryPersonName() {
        return deliveryPersonName;
    }

    public void setDeliveryPersonName(String deliveryPersonName) {
        this.deliveryPersonName = deliveryPersonName;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
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
     * 获取订单号
     *
     * @return order_id - 订单号
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 设置订单号
     *
     * @param orderId 订单号
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


    /**
     * 获取sku
     *
     * @return sku - sku
     */
    public String getSku() {
        return sku;
    }

    /**
     * 设置sku
     *
     * @param sku sku
     */
    public void setSku(String sku) {
        this.sku = sku;
    }

    /**
     * 获取数量
     *
     * @return quantity - 数量
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * 设置数量
     *
     * @param quantity 数量
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * 获取运费
     *
     * @return shipping_fee - 运费
     */
    public BigDecimal getShippingFee() {
        return shippingFee;
    }

    /**
     * 设置运费
     *
     * @param shippingFee 运费
     */
    public void setShippingFee(BigDecimal shippingFee) {
        this.shippingFee = shippingFee;
    }

    /**
     * 获取包装费
     *
     * @return packaging_fee - 包装费
     */
    public BigDecimal getPackagingFee() {
        return packagingFee;
    }

    /**
     * 设置包装费
     *
     * @param packagingFee 包装费
     */
    public void setPackagingFee(BigDecimal packagingFee) {
        this.packagingFee = packagingFee;
    }

    /**
     * 获取单价
     *
     * @return unit_price - 单价
     */
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    /**
     * 设置单价
     *
     * @param unitPrice 单价
     */
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * 获取创建人
     *
     * @return creator - 创建人
     */
    public Integer getCreator() {
        return creator;
    }

    /**
     * 设置创建人
     *
     * @param creator 创建人
     */
    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    /**
     * 获取创建时间
     *
     * @return create_date - 创建时间
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置创建时间
     *
     * @param createDate 创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取更新时间
     *
     * @return update_date - 更新时间
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 设置更新时间
     *
     * @param updateDate 更新时间
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * 获取更新人
     *
     * @return updator - 更新人
     */
    public Integer getUpdator() {
        return updator;
    }

    /**
     * 设置更新人
     *
     * @param updator 更新人
     */
    public void setUpdator(Integer updator) {
        this.updator = updator;
    }

    /**
     * 获取代理id
     *
     * @return agent_id - 代理id
     */
    public Integer getAgentId() {
        return agentId;
    }

    /**
     * 设置代理id
     *
     * @param agentId 代理id
     */
    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    /**
     * 获取(GIFT赠品,SALES销售商品)
     *
     * @return type - (GIFT赠品,SALES销售商品)
     */
    public String getType() {
        return type;
    }

    /**
     * 设置(GIFT赠品,SALES销售商品)
     *
     * @param type (GIFT赠品,SALES销售商品)
     */
    public void setType(String type) {
        this.type = type;
    }
}