package com.mszhan.redwine.manage.server.model.mszhanRedwineManage;

import java.util.Date;
import javax.persistence.*;

@Table(name = "user_login")
public class UserLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_name")
    private String userName;

    private String password;

    @Column(name = "agent_id")
    private Integer agentId;

    private String status;

    private String remark;

    /**
     * 使用人员名称
     */
    @Column(name = "person_name")
    private String personName;

    @Column(name = "create_date")
    private Date createDate;

    /**
     * 是否为系统管理员
     */
    @Column(name = "sys_user")
    private String sysUser;

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
     * @return user_name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return agent_id
     */
    public Integer getAgentId() {
        return agentId;
    }

    /**
     * @param agentId
     */
    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    /**
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取使用人员名称
     *
     * @return person_name - 使用人员名称
     */
    public String getPersonName() {
        return personName;
    }

    /**
     * 设置使用人员名称
     *
     * @param personName 使用人员名称
     */
    public void setPersonName(String personName) {
        this.personName = personName;
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
     * 获取是否为系统管理员
     *
     * @return sys_user - 是否为系统管理员
     */
    public String getSysUser() {
        return sysUser;
    }

    /**
     * 设置是否为系统管理员
     *
     * @param sysUser 是否为系统管理员
     */
    public void setSysUser(String sysUser) {
        this.sysUser = sysUser;
    }
}