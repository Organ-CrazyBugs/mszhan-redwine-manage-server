package com.mszhan.redwine.manage.server.model.mszhanRedwineManage;

import java.util.Date;
import javax.persistence.*;

public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 仓库id
     */
    @Column(name = "ware_house_id")
    private Integer wareHouseId;

    /**
     * 产品id
     */
    @Column(name = "product_id")
    private Integer productId;

    /**
     * sku
     */
    private String sku;

    /**
     * 库存数量
     */
    private Integer quantity;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 创建人
     */
    private Integer creator;

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
     * 获取仓库id
     *
     * @return ware_house_id - 仓库id
     */
    public Integer getWareHouseId() {
        return wareHouseId;
    }

    /**
     * 设置仓库id
     *
     * @param wareHouseId 仓库id
     */
    public void setWareHouseId(Integer wareHouseId) {
        this.wareHouseId = wareHouseId;
    }

    /**
     * 获取产品id
     *
     * @return product_id - 产品id
     */
    public Integer getProductId() {
        return productId;
    }

    /**
     * 设置产品id
     *
     * @param productId 产品id
     */
    public void setProductId(Integer productId) {
        this.productId = productId;
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
     * 获取库存数量
     *
     * @return quantity - 库存数量
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * 设置库存数量
     *
     * @param quantity 库存数量
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
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
}