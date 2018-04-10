package com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query;

import java.math.BigDecimal;

/**
 * Created by god on 2018/4/7.
 */
public class AgentsUpdatePojo {

    private Integer id;

    private String operationType;

    private BigDecimal balance;

    private String paymentType;

    private String remark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
