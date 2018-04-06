package com.mszhan.redwine.manage.server.service.impl;

import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.InventoryMapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Inventory;
import com.mszhan.redwine.manage.server.service.InventoryService;
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
public class InventoryServiceImpl extends AbstractService<Inventory> implements InventoryService {
    @Resource
    private InventoryMapper inventoryMapper;

}
