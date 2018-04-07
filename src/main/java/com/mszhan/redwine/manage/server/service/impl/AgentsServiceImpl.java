package com.mszhan.redwine.manage.server.service.impl;

import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.AgentPriceHistoryMapper;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.AgentsMapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.AgentPriceHistory;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Agents;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.base.PaginateResult;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.AgentQuery;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.AgentsUpdatePojo;
import com.mszhan.redwine.manage.server.service.AgentsService;
import com.mszhan.redwine.manage.server.core.AbstractService;
import com.mszhan.redwine.manage.server.util.ResponseUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;


/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 21:50 2018/04/06
 */
@Service
@Transactional
public class AgentsServiceImpl extends AbstractService<Agents> implements AgentsService {
    @Resource
    private AgentsMapper agentsMapper;

    @Autowired
    private AgentPriceHistoryMapper agentPriceHistoryMapper;


    @Override
    public ResponseUtils.ResponseVO queryForPage(AgentQuery query) {
        PaginateResult<Agents> tableData = new PaginateResult();
        Integer count = agentsMapper.queryCount(query);
        if (count == null || count.equals(0)){
            tableData.setRows(new ArrayList<>());
            tableData.setTotal(0L);
            return ResponseUtils.newResponse().succeed(tableData);
        }
        tableData.setTotal(Long.valueOf(count));
        List<Agents> list = agentsMapper.queryForPage(query);
        tableData.setRows(list);
        return ResponseUtils.newResponse().succeed(tableData);
    }

    @Override
    public ResponseUtils.ResponseVO updateAgent(Agents agents) {

        return null;
    }

    private void agentTrimToNull(Agents agents) {
        agents.setAddress(StringUtils.trimToNull(agents.getAddress()));
        agents.setName(StringUtils.trimToNull(agents.getName()));
        agents.setPhone(StringUtils.trimToNull(agents.getPhone()));
        agents.setTel(StringUtils.trimToNull(agents.getTel()));

    }

    @Override
    public ResponseUtils.ResponseVO updateBalance(AgentsUpdatePojo agentsUpdatePojo) {
        if (agentsUpdatePojo.getId() == null){
            return ResponseUtils.newResponse().failed(500, "id不能为空");
        }
        if (agentsUpdatePojo.getBalance() == null){
            return ResponseUtils.newResponse().failed(500, "余额不能为空");
        }
        if (StringUtils.isBlank(agentsUpdatePojo.getPaymentType())){
            return ResponseUtils.newResponse().failed(500, "支付方式不能为空");
        }
        if (StringUtils.isBlank(agentsUpdatePojo.getOperationType())){
            return ResponseUtils.newResponse().failed(500, "操作类型不能为空");
        }
        Date nowDate = new Date();
        AgentPriceHistory history = new AgentPriceHistory();
        history.setType(agentsUpdatePojo.getOperationType());
        history.setPaymentType(agentsUpdatePojo.getPaymentType());
        history.setPrice(agentsUpdatePojo.getBalance());
        history.setCreateDate(nowDate);
        history.setCreator(1);
        history.setAgentId(agentsUpdatePojo.getId());
        Agents agents = agentsMapper.selectByPrimaryKey(agentsUpdatePojo.getId());
        if (agents == null){
            return ResponseUtils.newResponse().failed(500, "没有找到对应代理人");
        }
        BigDecimal balance = agents.getBalance();
        if (agentsUpdatePojo.getOperationType().equals("add")){
            balance = balance.add(agentsUpdatePojo.getBalance());
        } else {
            if (balance.compareTo(agentsUpdatePojo.getBalance()) < 0){
                return ResponseUtils.newResponse().failed(500, "余额小于要扣金额，不可操作");
            }
            balance = balance.subtract(agentsUpdatePojo.getBalance());
        }
        agentsMapper.updateBalance(agentsUpdatePojo.getId(), balance);
        agentPriceHistoryMapper.insert(history);
        return ResponseUtils.newResponse().succeed();
    }

    @Override
    public ResponseUtils.ResponseVO addAgent(Agents agents) {
        if (StringUtils.isBlank(agents.getAddress())){
            return ResponseUtils.newResponse().failed(500, "地址不能为空");
        }
        if (StringUtils.isBlank(agents.getName())){
            return ResponseUtils.newResponse().failed(500, "名字不能为空");
        }
        if (StringUtils.isBlank(agents.getPhone())){
            return ResponseUtils.newResponse().failed(500, "电话号码不能为空");
        }
        if (StringUtils.isBlank(agents.getTel())){
            return ResponseUtils.newResponse().failed(500, "手机号码不能为空");
        }
        if (agents.getBalance() == null){
            return ResponseUtils.newResponse().failed(500, "余额不能为空");
        }
        agentTrimToNull(agents);
        agentsMapper.insert(agents);
        return ResponseUtils.newResponse().succeed();
    }

    @Override
    public ResponseUtils.ResponseVO delAgent(Integer id) {
        return null;
    }
}
