package com.mszhan.redwine.manage.server.service;

import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Agents;
import com.mszhan.redwine.manage.server.core.Service;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.AgentQuery;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.AgentsUpdatePojo;
import com.mszhan.redwine.manage.server.util.ResponseUtils;

/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 21:50 2018/04/06
 */
public interface AgentsService extends Service<Agents> {

    ResponseUtils.ResponseVO queryForPage(AgentQuery query);

    ResponseUtils.ResponseVO updateAgent(Agents agents);

    ResponseUtils.ResponseVO updateBalance(AgentsUpdatePojo agentsUpdatePojo);

    ResponseUtils.ResponseVO addAgent(Agents agents);

    ResponseUtils.ResponseVO delAgent(Integer id);

}
