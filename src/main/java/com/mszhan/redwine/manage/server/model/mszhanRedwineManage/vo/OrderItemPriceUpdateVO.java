package com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo;

import java.math.BigDecimal;
import java.util.List;

public class OrderItemPriceUpdateVO {
    private String orderId;
    private List<PriceItem> items;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<PriceItem> getItems() {
        return items;
    }

    public void setItems(List<PriceItem> items) {
        this.items = items;
    }

    public static class PriceItem {
        private Integer orderItemId;
        private BigDecimal price;

        public Integer getOrderItemId() {
            return orderItemId;
        }

        public void setOrderItemId(Integer orderItemId) {
            this.orderItemId = orderItemId;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }
    }
}
