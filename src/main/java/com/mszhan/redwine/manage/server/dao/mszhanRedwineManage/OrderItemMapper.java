package com.mszhan.redwine.manage.server.dao.mszhanRedwineManage;

import com.mszhan.redwine.manage.server.core.Mapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.OrderItem;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo.SelectOrderItemVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemMapper extends Mapper<OrderItem> {

    List<OrderItem> fetchOrderItemsByOrderId(@Param("orderIds") List<String> orderIds);

    OrderItem fetchOrderGiftByOrderId(String orderId);

}