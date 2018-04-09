package com.mszhan.redwine.manage.server.dao.mszhanRedwineManage;

import com.mszhan.redwine.manage.server.core.Mapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.OrderHeader;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.OrderQuery;
import org.springframework.core.annotation.Order;

import java.util.List;

public interface OrderHeaderMapper extends Mapper<OrderHeader> {

    Integer queryCount(OrderQuery query);

    List<OrderHeader> queryForPage(OrderQuery query);
}