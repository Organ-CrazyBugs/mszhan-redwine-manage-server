package com.mszhan.redwine.manage.server.service;

import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Warehouse;
import com.mszhan.redwine.manage.server.core.Service;

import java.util.List;

/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 21:50 2018/04/06
 */
public interface WarehouseService extends Service<Warehouse> {

    enum WarehouseStatus {
        ENABLED,DISABLED;
    }

    Warehouse createWarehouse(Integer userId, Warehouse warehouse);

    void changeStatus(Integer userId, List<Integer> warehouseIds, WarehouseStatus status);

}
