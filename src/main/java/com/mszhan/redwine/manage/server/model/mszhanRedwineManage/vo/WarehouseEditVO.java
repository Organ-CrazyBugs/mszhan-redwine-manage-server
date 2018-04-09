package com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo;

/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 16:44 2018/4/8
 */
public class WarehouseEditVO {
    private Integer id;
    private String address;
    private String name;
    private String phone;
    private String principal;
    private String remark;
    private String tel;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
