package com.mszhan.redwine.manage.server.service.impl;

import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.BillMapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Bill;
import com.mszhan.redwine.manage.server.service.BillService;
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
public class BillServiceImpl extends AbstractService<Bill> implements BillService {
    @Resource
    private BillMapper billMapper;

}
