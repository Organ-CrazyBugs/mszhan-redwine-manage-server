package com.mszhan.redwine.manage.server.service;

import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Inventory;
import com.mszhan.redwine.manage.server.core.Service;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.InventoryQuery;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo.InventoryInputVO;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo.InventoryOutputVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 21:50 2018/04/06
 */
public interface InventoryService extends Service<Inventory> {
    void inventoryInput(InventoryInputVO vo);

    void inventoryOutput(InventoryOutputVO vo);

    void leadOutOutboundDetail(InventoryQuery query, HttpServletResponse res);

    void leadOutInboundDetail(InventoryQuery query, HttpServletResponse res);

    void leadOutInventory(InventoryQuery query, HttpServletResponse res);
}
