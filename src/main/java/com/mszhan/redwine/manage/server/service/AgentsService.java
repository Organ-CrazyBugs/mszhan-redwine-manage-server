package com.mszhan.redwine.manage.server.service;

import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Agents;
import com.mszhan.redwine.manage.server.core.Service;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.base.PaginateResult;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.AgentQuery;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.AgentsUpdatePojo;

/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 21:50 2018/04/06
 */
public interface AgentsService extends Service<Agents> {

    PaginateResult<Agents> queryForPage(AgentQuery query);

    void updateAgent(Agents agents);

    Agents queryById(Integer id);

    void updateBalance(AgentsUpdatePojo agentsUpdatePojo);

    void addAgent(Agents agents);

    void delAgent(Integer id);

}
