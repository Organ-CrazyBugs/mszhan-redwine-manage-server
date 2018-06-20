package com.mszhan.redwine.manage.server.service;

import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.OrderHeader;
import com.mszhan.redwine.manage.server.core.Service;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.base.PaginateResult;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.AddOrderPojo;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.OrderQuery;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo.CreateOrderVO;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo.OrderCancelVO;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo.OrderItemPriceUpdateVO;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo.OrderMarkPaymentVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 21:50 2018/04/06
 */
public interface OrderHeaderService extends Service<OrderHeader> {

    PaginateResult<OrderHeader> queryForPage(OrderQuery query);

    OrderHeader createOrder(CreateOrderVO orderVO);

    void orderMarkShipped(List<String> orderIds);

    void orderMarkPayment(OrderMarkPaymentVO paymentVO);

    void orderCancel(OrderCancelVO cancelVO);

    void addOrder(AddOrderPojo addOrderPojo);

    void updateItemPrice(OrderItemPriceUpdateVO vo);

    void leadOutOrderOutboundExcel(HttpServletResponse response, OrderQuery query);
}
