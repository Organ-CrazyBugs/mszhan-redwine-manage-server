package com.mszhan.redwine.manage.server.dao.mszhanRedwineManage;

import com.mszhan.redwine.manage.server.core.Mapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Warehouse;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface WarehouseMapper extends Mapper<Warehouse> {

    @Select({"SELECT id, name FROM warehouse"})
    List<Map<String, Object>> fetchAllWarehouse();

}