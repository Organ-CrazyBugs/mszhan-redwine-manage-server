package com.mszhan.redwine.manage.server.dao.mszhanRedwineManage;

import com.mszhan.redwine.manage.server.core.Mapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Agents;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.AgentQuery;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface AgentsMapper extends Mapper<Agents> {

    Integer queryCount(AgentQuery query);

    List<Agents> queryForPage(AgentQuery query);

    List<Agents> queryByPhone(@Param("phone") String phone);

    List<Agents> queryByPhoneAndNotInId(@Param("phone") String phone, @Param("id") Integer id);

    @Select({"SELECT id,name,phone FROM agents"})
    List<Map<String, Object>> fetchAllAgents();

    void deleteAgents(List<Integer> idList);

    void updateBalance(@Param("id") Integer id, @Param("balance") BigDecimal balance);

    void updateAgents(Agents agents);

}