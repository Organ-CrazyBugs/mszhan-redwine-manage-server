package com.mszhan.redwine.manage.server.model.mszhanRedwineManage;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 产品名称
     */
    @Column(name = "product_name")
    private String productName;

    /**
     * 成本
     */
    private BigDecimal cost;

    /**
     * 总代理价
     */
    @Column(name = "`general_gent_ price`")
    private BigDecimal generalGentPrice;

    /**
     * 代理价
     */
    @Column(name = "gent_price")
    private BigDecimal gentPrice;

    /**
     * 批发价
     */
    @Column(name = "wholesale_price")
    private BigDecimal wholesalePrice;

    /**
     * 零售价
     */
    @Column(name = "retail_price")
    private BigDecimal retailPrice;

    /**
     * 单位
     */
    private String unit;

    /**
     * 规格
     */
    private String specification;

    /**
     * 级别
     */
    private String level;

    /**
     * 产区
     */
    @Column(name = "production_area")
    private String productionArea;

    /**
     * 图片地址
     */
    @Column(name = "product_url")
    private String productUrl;

    /**
     * sku
     */
    private String sku;

    private Integer creator;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_date")
    private Date updateDate;

    private Integer updator;

    /**
     * 背景
     */
    @Column(name = "back_remark")
    private String backRemark;

    /**
     * 品牌
     */
    @Column(name = "brand_name")
    private String brandName;

    /**
     * 酒精度
     */
    @Column(name = "alcohol_content")
    private BigDecimal alcoholContent;

    private String remove;


    public String getRemove() {
        return remove;
    }

    public void setRemove(String remove) {
        this.remove = remove;
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
     * 获取产品名称
     *
     * @return product_name - 产品名称
     */
    public String getProductName() {
        return productName;
    }

    /**
     * 设置产品名称
     *
     * @param productName 产品名称
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * 获取成本
     *
     * @return cost - 成本
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * 设置成本
     *
     * @param cost 成本
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * 获取总代理价
     *
     * @return general_gent_ price - 总代理价
     */
    public BigDecimal getGeneralGentPrice() {
        return generalGentPrice;
    }

    /**
     * 设置总代理价
     *
     * @param generalGentPrice 总代理价
     */
    public void setGeneralGentPrice(BigDecimal generalGentPrice) {
        this.generalGentPrice = generalGentPrice;
    }

    /**
     * 获取代理价
     *
     * @return gent_price - 代理价
     */
    public BigDecimal getGentPrice() {
        return gentPrice;
    }

    /**
     * 设置代理价
     *
     * @param gentPrice 代理价
     */
    public void setGentPrice(BigDecimal gentPrice) {
        this.gentPrice = gentPrice;
    }

    /**
     * 获取批发价
     *
     * @return wholesale_price - 批发价
     */
    public BigDecimal getWholesalePrice() {
        return wholesalePrice;
    }

    /**
     * 设置批发价
     *
     * @param wholesalePrice 批发价
     */
    public void setWholesalePrice(BigDecimal wholesalePrice) {
        this.wholesalePrice = wholesalePrice;
    }

    /**
     * 获取零售价
     *
     * @return retail_price - 零售价
     */
    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    /**
     * 设置零售价
     *
     * @param retailPrice 零售价
     */
    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    /**
     * 获取单位
     *
     * @return unit - 单位
     */
    public String getUnit() {
        return unit;
    }

    /**
     * 设置单位
     *
     * @param unit 单位
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * 获取规格
     *
     * @return specification - 规格
     */
    public String getSpecification() {
        return specification;
    }

    /**
     * 设置规格
     *
     * @param specification 规格
     */
    public void setSpecification(String specification) {
        this.specification = specification;
    }

    /**
     * 获取级别
     *
     * @return level - 级别
     */
    public String getLevel() {
        return level;
    }

    /**
     * 设置级别
     *
     * @param level 级别
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     * 获取产区
     *
     * @return production_area - 产区
     */
    public String getProductionArea() {
        return productionArea;
    }

    /**
     * 设置产区
     *
     * @param productionArea 产区
     */
    public void setProductionArea(String productionArea) {
        this.productionArea = productionArea;
    }

    /**
     * 获取图片地址
     *
     * @return product_url - 图片地址
     */
    public String getProductUrl() {
        return productUrl;
    }

    /**
     * 设置图片地址
     *
     * @param productUrl 图片地址
     */
    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
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
     * @return update_date
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * @return updator
     */
    public Integer getUpdator() {
        return updator;
    }

    /**
     * @param updator
     */
    public void setUpdator(Integer updator) {
        this.updator = updator;
    }

    /**
     * 获取背景
     *
     * @return back_remark - 背景
     */
    public String getBackRemark() {
        return backRemark;
    }

    /**
     * 设置背景
     *
     * @param backRemark 背景
     */
    public void setBackRemark(String backRemark) {
        this.backRemark = backRemark;
    }

    /**
     * 获取品牌
     *
     * @return brand_name - 品牌
     */
    public String getBrandName() {
        return brandName;
    }

    /**
     * 设置品牌
     *
     * @param brandName 品牌
     */
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    /**
     * 获取酒精度
     *
     * @return alcohol_content - 酒精度
     */
    public BigDecimal getAlcoholContent() {
        return alcoholContent;
    }

    /**
     * 设置酒精度
     *
     * @param alcoholContent 酒精度
     */
    public void setAlcoholContent(BigDecimal alcoholContent) {
        this.alcoholContent = alcoholContent;
    }
}