package com.mszhan.redwine.manage.server.service;

import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Inventory;
import com.mszhan.redwine.manage.server.core.Service;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo.InventoryInputVO;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo.InventoryOutputVO;

/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 21:50 2018/04/06
 */
public interface InventoryService extends Service<Inventory> {
    void inventoryInput(InventoryInputVO vo);

    void inventoryOutput(InventoryOutputVO vo);
}
