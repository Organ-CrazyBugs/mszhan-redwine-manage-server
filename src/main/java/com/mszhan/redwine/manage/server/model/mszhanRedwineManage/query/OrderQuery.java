package com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class OrderQuery extends PageQuery {

    private Integer agentId;

    private String orderId;

    private String sku;

    private String productName;

    private String brandName;

    private String orderStatus;

    private String paymentStatus;

    private String createStartDate;

    private String createEndDate;

    private String deliveryStartDate;

    private String deliveryEndDate;

    private String clientName;

    private Integer warehouseId;


    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getBrandName() {
        if (!StringUtils.isBlank(brandName)){
            return String.format("%%%s%%", StringUtils.trimToNull(brandName));
        }
        return StringUtils.trimToNull(brandName);
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

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

    public String getDeliveryStartDate() {
        String startDate = StringUtils.trimToNull(this.deliveryStartDate);
        if (StringUtils.isNotBlank(startDate)){
            startDate = String.format("%s 00:00:00", startDate);
        }
        return startDate;
    }

    public String getDeliveryEndDate() {
        String endDate = StringUtils.trimToNull(this.deliveryEndDate);
        if (StringUtils.isNotBlank(endDate)){
            endDate = String.format("%s 23:59:59", endDate);
        }
        return endDate;
    }

    public void setDeliveryStartDate(String deliveryStartDate) {
        this.deliveryStartDate = deliveryStartDate;
    }


    public void setDeliveryEndDate(String deliveryEndDate) {
        this.deliveryEndDate = deliveryEndDate;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getOrderId() {
        return StringUtils.trimToNull(orderId);
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSku() {
        return StringUtils.trimToNull(sku);
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProductName() {
        if (!StringUtils.isBlank(productName)){
            return String.format("%%%s%%", StringUtils.trimToNull(productName));
        }
        return StringUtils.trimToNull(productName);
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }


    public String getPaymentStatus() {
        return StringUtils.trimToNull(paymentStatus);
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
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
