package com.mszhan.redwine.manage.server.model.mszhanRedwineManage;

import javax.persistence.*;

@Table(name = "order_delivery_person")
public class OrderDeliveryPerson {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "order_item_id")
    private Integer orderItemId;

    @Column(name = "delivery_person")
    private String deliveryPerson;

    private String tel;

    private String phone;

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
     * @return order_id
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * @return order_item_id
     */
    public Integer getOrderItemId() {
        return orderItemId;
    }

    /**
     * @param orderItemId
     */
    public void setOrderItemId(Integer orderItemId) {
        this.orderItemId = orderItemId;
    }

    /**
     * @return delivery_person
     */
    public String getDeliveryPerson() {
        return deliveryPerson;
    }

    /**
     * @param deliveryPerson
     */
    public void setDeliveryPerson(String deliveryPerson) {
        this.deliveryPerson = deliveryPerson;
    }

    /**
     * @return tel
     */
    public String getTel() {
        return tel;
    }

    /**
     * @param tel
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }
}