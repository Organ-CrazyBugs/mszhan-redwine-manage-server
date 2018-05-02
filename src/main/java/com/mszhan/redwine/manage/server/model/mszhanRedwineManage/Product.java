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
    @Column(name = "`general_gent_price`")
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
     * 图片名称
     */
    @Column(name = "product_file_name")
    private String productFileName;

    @Column(name = "file_path")
    private String filePath;

    /**
     * sku
     */
    private String sku;

    private Integer creator;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "creator_name")
    private String creatorName;

    @Column(name = "updator_name")
    private String updatorName;

    @Column(name = "update_date")
    private Date updateDate;

    private Integer updator;


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
    /**
     * 原产国
     */
    @Column(name = "origin_country")
    private String originCountry;

    /**
     * 树龄
     */
    @Column(name = "tree_age")
    private String treeAge;
    /**
     * 葡萄酒类型
     */
    @Column(name = "wine_type")
    private String wineType;

    /**
     * 贮藏方式
     */
    @Column(name = "storage_method")
    private String storageMethod;

    /**
     * 美食搭配
     */
    @Column(name = "with_food")
    private String withFood;

    /**
     * 葡萄酒品种
     */
    @Column(name = "category")
    private String category;

    /**
     * 年份
     */
    @Column(name = "age")
    private String age;

    /**
     * 净含量
     */
    @Column(name = "net_weight")
    private BigDecimal netWeight;

    /**
     * 酿酒时间
     */
    @Column(name = "making_time")
    private String makingTime;

    /**
     * 品鉴记录
     */
    @Column(name = "tasting_records")
    private String tastingRecords;

    /**
     * 推荐理由
     */
    @Column(name = "recommended_reason")
    private String recommendedReason;

    /**
     * 品牌背景
     */
    @Column(name = "brand_backgroud")
    private String brandBackgroud;

    /**
     * 培育期
     */
    @Column(name= "incubation_period")
    private String incubationPeriod;

    /**
     * 介绍图片
     */
    @Column(name = "background_file_name")
    private String backgroundFileName;

    @Column(name = "background_path")
    private String backgroundPath;

    public String getUpdatorName() {
        return updatorName;
    }

    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }

    public String getIncubationPeriod() {
        return incubationPeriod;
    }

    public void setIncubationPeriod(String incubationPeriod) {
        this.incubationPeriod = incubationPeriod;
    }

    public String getCreatorName() {

        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getTreeAge() {
        return treeAge;
    }

    public void setTreeAge(String treeAge) {
        this.treeAge = treeAge;
    }

    public String getWineType() {
        return wineType;
    }

    public void setWineType(String wineType) {
        this.wineType = wineType;
    }

    public String getStorageMethod() {
        return storageMethod;
    }

    public void setStorageMethod(String storageMethod) {
        this.storageMethod = storageMethod;
    }

    public String getWithFood() {
        return withFood;
    }

    public void setWithFood(String withFood) {
        this.withFood = withFood;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public BigDecimal getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(BigDecimal netWeight) {
        this.netWeight = netWeight;
    }

    public String getMakingTime() {
        return makingTime;
    }

    public void setMakingTime(String makingTime) {
        this.makingTime = makingTime;
    }

    public String getTastingRecords() {
        return tastingRecords;
    }

    public void setTastingRecords(String tastingRecords) {
        this.tastingRecords = tastingRecords;
    }

    public String getRecommendedReason() {
        return recommendedReason;
    }

    public void setRecommendedReason(String recommendedReason) {
        this.recommendedReason = recommendedReason;
    }

    public String getBrandBackgroud() {
        return brandBackgroud;
    }

    public void setBrandBackgroud(String brandBackgroud) {
        this.brandBackgroud = brandBackgroud;
    }


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

    public String getProductFileName() {
        return productFileName;
    }

    public void setProductFileName(String productFileName) {
        this.productFileName = productFileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getBackgroundFileName() {
        return backgroundFileName;
    }

    public void setBackgroundFileName(String backgroundFileName) {
        this.backgroundFileName = backgroundFileName;
    }

    public String getBackgroundPath() {
        return backgroundPath;
    }

    public void setBackgroundPath(String backgroundPath) {
        this.backgroundPath = backgroundPath;
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