package com.mszhan.redwine.manage.server.dao.mszhanRedwineManage;

import com.mszhan.redwine.manage.server.core.Mapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Agents;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.AgentQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;

public interface AgentsMapper extends Mapper<Agents> {

    Integer queryCount(AgentQuery query);

    List<Agents> queryForPage(AgentQuery query);

    List<Agents> queryByTelAndNotInId(@Param("tel") String tel, @Param("id") Integer id);

    void updateBalance(@Param("id") Integer id, @Param("balance") BigDecimal balance);

    void updateAgents(Agents agents);

}