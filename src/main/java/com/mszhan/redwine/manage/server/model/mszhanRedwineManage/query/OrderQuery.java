package com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class OrderQuery extends PageQuery {

    private String orderIds;

    private String clientNames;

    private String skus;

    private String agentNames;

    private String deliveryPersonNames;

    private List<String> clientNameList;

    private List<String> skuList;

    private List<String> agentNameList;

    private List<String> deliveryPersonNameList;

    private List<String> orderIdList;

    public String getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(String orderIds) {
        this.orderIds = orderIds;
    }

    public String getClientNames() {
        return clientNames;
    }

    public void setClientNames(String clientNames) {
        this.clientNames = clientNames;
    }

    public String getSkus() {
        return skus;
    }

    public void setSkus(String skus) {
        this.skus = skus;
    }

    public String getAgentNames() {
        return agentNames;
    }

    public void setAgentNames(String agentNames) {
        this.agentNames = agentNames;
    }

    public String getDeliveryPersonNames() {
        return deliveryPersonNames;
    }

    public void setDeliveryPersonNames(String deliveryPersonNames) {
        this.deliveryPersonNames = deliveryPersonNames;
    }

    public List<String> getClientNameList() {
        return fetchObjList(clientNames, clientNameList);
    }

    public void setClientNameList(List<String> clientNameList) {
        this.clientNameList = clientNameList;
    }

    public List<String> getSkuList() {
        return fetchObjList(skus, skuList);
    }

    public void setSkuList(List<String> skuList) {
        this.skuList = skuList;
    }

    public List<String> getAgentNameList() {
        return fetchObjList(agentNames, agentNameList);
    }

    public void setAgentNameList(List<String> agentNameList) {
        this.agentNameList = agentNameList;
    }

    public List<String> getDeliveryPersonNameList() {
        return fetchObjList(deliveryPersonNames, deliveryPersonNameList);
    }

    public void setDeliveryPersonNameList(List<String> deliveryPersonNameList) {
        this.deliveryPersonNameList = deliveryPersonNameList;
    }

    public List<String> getOrderIdList() {
        return fetchObjList(orderIds, orderIdList);
    }

    public void setOrderIdList(List<String> orderIdList) {
        this.orderIdList = orderIdList;
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
