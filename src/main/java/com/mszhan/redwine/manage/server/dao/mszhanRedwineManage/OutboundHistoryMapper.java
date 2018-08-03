package com.mszhan.redwine.manage.server.dao.mszhanRedwineManage;

import com.mszhan.redwine.manage.server.core.Mapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.OutboundHistory;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.InventoryQuery;

import java.util.List;
import java.util.Map;

public interface OutboundHistoryMapper extends Mapper<OutboundHistory> {

    List<Map<String, Object>> leadOutOutboundDetail(InventoryQuery query);


}