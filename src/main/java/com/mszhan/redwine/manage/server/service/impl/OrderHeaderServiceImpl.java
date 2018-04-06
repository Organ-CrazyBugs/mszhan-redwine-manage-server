package com.mszhan.redwine.manage.server.service.impl;

import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.OrderHeaderMapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.OrderHeader;
import com.mszhan.redwine.manage.server.service.OrderHeaderService;
import com.mszhan.redwine.manage.server.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


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

}
