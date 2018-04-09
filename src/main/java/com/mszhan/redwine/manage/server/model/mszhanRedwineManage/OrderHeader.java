package com.mszhan.redwine.manage.server.model.mszhanRedwineManage;

import java.util.Date;
import javax.persistence.*;

@Table(name = "order_header")
public class OrderHeader {
    /**
     * 订单号
     */
    @Id
    @Column(name = "order_id")
    private String orderId;

    /**
     * 订单总金额
     */
    @Column(name = "total_amount")
    private Long totalAmount;

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
     * 更新人
     */
    private Integer updator;

    /**
     * 更新时间
     */
    @Column(name = "update_date")
    private Date updateDate;

    /**
     * 客户名称
     */
    @Column(name = "client_name")
    private String clientName;

    /**
     * 代理id
     */
    @Column(name = "agent_id")
    private Integer agentId;

    @Column(name = "agent_name")
    private String agentName;
    /**
     * 客户地址
     */
    private String address;

    /**
     * 邮政编码
     */
    @Column(name = "postal_code")
    private String postalCode;

    /**
     * (WAIT_DEAL待处理，SHIPPED已发货，RECEIVED已收货)
     */
    private String status;

    /**
     * (PAID已付款，UNPAID未付款)
     */
    @Column(name = "payment_status")
    private String paymentStatus;

    /**
     * 备注
     */
    private String remark;

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
     * 获取订单总金额
     *
     * @return total_amount - 订单总金额
     */
    public Long getTotalAmount() {
        return totalAmount;
    }

    /**
     * 设置订单总金额
     *
     * @param totalAmount 订单总金额
     */
    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
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
     * 获取客户名称
     *
     * @return client_name - 客户名称
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * 设置客户名称
     *
     * @param clientName 客户名称
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    /**
     * 获取客户地址
     *
     * @return address - 客户地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置客户地址
     *
     * @param address 客户地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取邮政编码
     *
     * @return postal_code - 邮政编码
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * 设置邮政编码
     *
     * @param postalCode 邮政编码
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * 获取(WAIT_DEAL待处理，SHIPPED已发货，RECEIVED已收货)
     *
     * @return status - (WAIT_DEAL待处理，SHIPPED已发货，RECEIVED已收货)
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置(WAIT_DEAL待处理，SHIPPED已发货，RECEIVED已收货)
     *
     * @param status (WAIT_DEAL待处理，SHIPPED已发货，RECEIVED已收货)
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取(PAID已付款，UNPAID未付款)
     *
     * @return payment_status - (PAID已付款，UNPAID未付款)
     */
    public String getPaymentStatus() {
        return paymentStatus;
    }

    /**
     * 设置(PAID已付款，UNPAID未付款)
     *
     * @param paymentStatus (PAID已付款，UNPAID未付款)
     */
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
}