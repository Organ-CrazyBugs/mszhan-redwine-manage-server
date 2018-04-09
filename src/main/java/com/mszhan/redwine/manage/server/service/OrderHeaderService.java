package com.mszhan.redwine.manage.server.service;

import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.OrderHeader;
import com.mszhan.redwine.manage.server.core.Service;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.AddOrderPojo;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.OrderQuery;
import com.mszhan.redwine.manage.server.util.ResponseUtils;

/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 21:50 2018/04/06
 */
public interface OrderHeaderService extends Service<OrderHeader> {

    ResponseUtils.ResponseVO queryForPage(OrderQuery query);

    ResponseUtils.ResponseVO addOrder(AddOrderPojo addOrderPojo);
}
