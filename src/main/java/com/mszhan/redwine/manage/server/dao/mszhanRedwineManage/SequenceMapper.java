package com.mszhan.redwine.manage.server.dao.mszhanRedwineManage;

import com.mszhan.redwine.manage.server.core.Mapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Bill;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Sequence;
import org.apache.ibatis.annotations.Param;

public interface SequenceMapper extends Mapper<Sequence> {
    Sequence queryByTypeAndDate(@Param("type") String type, @Param("date") String date);

}