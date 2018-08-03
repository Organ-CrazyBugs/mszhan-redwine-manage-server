package com.mszhan.redwine.manage.server.dao.mszhanRedwineManage;

import com.mszhan.redwine.manage.server.core.Mapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.InboundHistory;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.InventoryQuery;

import java.util.List;
import java.util.Map;

public interface InboundHistoryMapper extends Mapper<InboundHistory> {

    List<Map<String, Object>> leadOutInboundDetail(InventoryQuery query);
}