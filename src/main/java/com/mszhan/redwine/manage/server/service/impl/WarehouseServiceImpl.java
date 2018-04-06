package com.mszhan.redwine.manage.server.service.impl;

import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.WarehouseMapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Warehouse;
import com.mszhan.redwine.manage.server.service.WarehouseService;
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
public class WarehouseServiceImpl extends AbstractService<Warehouse> implements WarehouseService {
    @Resource
    private WarehouseMapper warehouseMapper;

}
