package com.mszhan.redwine.manage.server.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mszhan.redwine.manage.server.core.BasicException;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.OrderHeaderMapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.OrderHeader;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.base.PaginateResult;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.AddOrderPojo;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.OrderQuery;
import com.mszhan.redwine.manage.server.service.OrderHeaderService;
import com.mszhan.redwine.manage.server.core.AbstractService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 21:50 2018/04/06
 */
@Service
@Transactional
public class OrderHeaderServiceImpl extends AbstractService<OrderHeader> implements OrderHeaderService {
    @Resource
    private OrderHeaderMapper orderHeaderMapper;

    @Override
    public PaginateResult<OrderHeader> queryForPage(OrderQuery query) {
        Integer count = orderHeaderMapper.queryCount(query);
        if (count == null || count.equals(0)){
            return PaginateResult.newInstance(0, new ArrayList<>());
        }
        List<OrderHeader> list = orderHeaderMapper.queryForPage(query);
        return PaginateResult.newInstance(Long.valueOf(count), list);
    }

    @Override
    public void addOrder(AddOrderPojo addOrderPojo) {
        if (StringUtils.isBlank(addOrderPojo.getOrderContent())){
            throw BasicException.newInstance().error("订单信息不能为空", 500);
        }
        if (StringUtils.isBlank(addOrderPojo.getOrderItemContents())){
            throw BasicException.newInstance().error("订单商品信息不能为空", 500);
        }
        ObjectMapper objMr = new ObjectMapper();
        OrderHeader orderHeader = null;
        try {
            orderHeader = objMr.readValue(addOrderPojo.getOrderContent(), OrderHeader.class);
        } catch (IOException e) {
            throw BasicException.newInstance().error("订单信息转化异常", 500);
        }
    }
}
