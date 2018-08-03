package com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by god on 2018/7/31.
 */
public class InventoryQuery extends PageQuery {

    private Integer warehouseId;

    private String sku;

    private String productName;

    private String brandName;

    private String createStartDate;

    private String createEndDate;


    public String getCreateStartDate() {
        String startDate = StringUtils.trimToNull(this.createStartDate);
        if (StringUtils.isNotBlank(startDate)){
            startDate = String.format("%s 00:00:00", startDate);
        }
        return startDate;
    }

    public String getCreateEndDate() {
        String endDate = StringUtils.trimToNull(this.createEndDate);
        if (StringUtils.isNotBlank(endDate)){
            endDate = String.format("%s 23:59:59", endDate);
        }
        return endDate;
    }

    public void setCreateStartDate(String createStartDate) {
        this.createStartDate = createStartDate;
    }


    public void setCreateEndDate(String createEndDate) {
        this.createEndDate = createEndDate;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }


    public void setProductName(String productName) {
        this.productName = productName;
    }


    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getSku() {
        return StringUtils.isBlank(sku) ? null : String.format("%%%s%%", StringUtils.trimToNull(sku));
    }


    public String getProductName() {
        return StringUtils.isBlank(productName) ? null : String.format("%%%s%%", StringUtils.trimToNull(productName));
    }


    public String getBrandName() {
        return StringUtils.isBlank(brandName) ? null : String.format("%%%s%%", StringUtils.trimToNull(brandName));
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
