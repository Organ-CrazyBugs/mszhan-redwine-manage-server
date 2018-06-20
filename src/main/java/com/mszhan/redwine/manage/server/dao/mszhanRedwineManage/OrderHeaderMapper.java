package com.mszhan.redwine.manage.server.dao.mszhanRedwineManage;

import com.mszhan.redwine.manage.server.core.Mapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.OrderHeader;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.OrderQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.core.annotation.Order;

import java.util.List;
import java.util.Map;

public interface OrderHeaderMapper extends Mapper<OrderHeader> {

    Integer queryCount(OrderQuery query);

    List<OrderHeader> fetchOrders(
            @Param("agentId") Integer agentId,
            @Param("orderId") String orderId,
            @Param("productName") String productName,
            @Param("sku") String sku,
            @Param("brandName") String brandName,
            @Param("orderStatus") String orderStatus,
            @Param("paymentStatus") String paymentStatus
        );

    List<OrderHeader> queryForPage(OrderQuery query);

    List<Map<String, Object>> queryForOutboundExcel(OrderQuery query);
}