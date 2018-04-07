package com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductQuery extends PageQuery {

    private String skus;
    private String productNames;
    private String productionAreas;
    private String brandNames;
    private String alcoholContent;
    private List<String> skuList;
    private List<String> productNameList;
    private List<String> brandNameList;

    public String getSkus() {
        return skus;
    }

    public void setSkus(String skus) {
        this.skus = skus;
    }

    public String getProductNames() {
        return productNames;
    }

    public void setProductNames(String productNames) {
        this.productNames = productNames;
    }

    public String getProductionAreas() {
        return productionAreas;
    }

    public void setProductionAreas(String productionAreas) {
        this.productionAreas = productionAreas;
    }

    public String getBrandNames() {
        return brandNames;
    }

    public void setBrandNames(String brandNames) {
        this.brandNames = brandNames;
    }

    public String getAlcoholContent() {
        return StringUtils.trimToNull(alcoholContent);
    }

    public void setAlcoholContent(String alcoholContent) {
        this.alcoholContent = alcoholContent;
    }

    public List<String> getSkuList() {
        return fetchObjList(skus, skuList);
    }

    public void setSkuList(List<String> skuList) {
        this.skuList = skuList;
    }

    public List<String> getProductNameList() {
        return fetchObjList(productNames, productNameList);
    }

    public void setProductNameList(List<String> productNameList) {
        this.productNameList = productNameList;
    }

    public List<String> getBrandNameList() {
        return fetchObjList(brandNames, brandNameList);
    }

    public void setBrandNameList(List<String> brandNameList) {
        this.brandNameList = brandNameList;
    }

    public List<String> fetchObjList(String obj, List<String> objList) {
        if (!CollectionUtils.isEmpty(objList)){
            return objList;
        }
        obj = StringUtils.trimToNull(obj);
        if (StringUtils.isNotBlank(obj)){
            objList = new ArrayList<>();
            String str[] = obj.split("\\,");
            for (String st : str){
                if (StringUtils.isNotBlank(st)){
                    objList.add(st);
                }
            }
        }
        return objList;
    }
}
