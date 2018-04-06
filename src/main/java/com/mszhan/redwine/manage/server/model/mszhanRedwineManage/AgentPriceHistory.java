package com.mszhan.redwine.manage.server.model.mszhanRedwineManage;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "agent_price_history")
public class AgentPriceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "create_date")
    private Date createDate;

    private BigDecimal price;

    private Integer creator;

    /**
     * (RECHARGE充值，SPEND花费)
     */
    private String type;

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
     * @return price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
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
     * 获取(RECHARGE充值，SPEND花费)
     *
     * @return type - (RECHARGE充值，SPEND花费)
     */
    public String getType() {
        return type;
    }

    /**
     * 设置(RECHARGE充值，SPEND花费)
     *
     * @param type (RECHARGE充值，SPEND花费)
     */
    public void setType(String type) {
        this.type = type;
    }
}