package com.mszhan.redwine.manage.server.dao.mszhanRedwineManage;

import com.mszhan.redwine.manage.server.core.Mapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Inventory;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.InventoryQuery;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.vo.FetchInventoryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface InventoryMapper extends Mapper<Inventory> {

    List<FetchInventoryVO> fetchInventory(
            @Param("warehouseId") Integer warehouseId,
            @Param("sku") String sku,
            @Param("productName") String productName,
            @Param("brandName") String brandName
       );

    List<FetchInventoryVO> queryForLeadOut(InventoryQuery query);

}