package com.mszhan.redwine.manage.server.service.impl;

import com.mszhan.redwine.manage.server.core.BasicException;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.AgentPriceHistoryMapper;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.AgentsMapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.AgentPriceHistory;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Agents;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.base.PaginateResult;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.AgentQuery;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.AgentsUpdatePojo;
import com.mszhan.redwine.manage.server.service.AgentsService;
import com.mszhan.redwine.manage.server.core.AbstractService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    public PaginateResult<Agents> queryForPage(AgentQuery query) {
        Integer count = agentsMapper.queryCount(query);
        if (count == null || count.equals(0)){
            return PaginateResult.newInstance(0, new ArrayList<>());
        }
        List<Agents> list = agentsMapper.queryForPage(query);
        return PaginateResult.newInstance(Long.valueOf(count), list);
    }

    @Override
    public void updateAgent(Agents agents) {
        if (agents.getId() == null){
            throw BasicException.newInstance().error("id不能为空", 500);
        }
        if (StringUtils.isBlank(agents.getAddress())){
            throw BasicException.newInstance().error("地址不能为空", 500);
        }
        if (StringUtils.isBlank(agents.getType())){
            throw BasicException.newInstance().error("代理类型不能为空", 500);
        }
        if (StringUtils.isBlank(agents.getName())){
            throw BasicException.newInstance().error("名字不能为空", 500);
        }
        if (StringUtils.isBlank(agents.getPhone())){
            throw BasicException.newInstance().error("手机号码不能为空", 500);
        }
        if (StringUtils.isBlank(agents.getTel())){
            throw BasicException.newInstance().error("座机号码不能为空", 500);
        }
        agentTrimToNull(agents);
        List<Agents> agentsList = agentsMapper.queryByPhoneAndNotInId(agents.getPhone(), agents.getId());
        if (!CollectionUtils.isEmpty(agentsList)){
            throw BasicException.newInstance().error("已经存在该电话号码，不可重复添加", 500);
        }
        Agents queryAgent = agentsMapper.selectByPrimaryKey(agents.getId());
        Date nowDate = new Date();
        queryAgent.setTel(agents.getTel());
        queryAgent.setName(agents.getName());
        queryAgent.setAddress(agents.getAddress());
        queryAgent.setType(agents.getType());
        queryAgent.setUpdateDate(nowDate);
        queryAgent.setUpdatorName("1");
        queryAgent.setUpdator(1);
        agentsMapper.updateAgents(queryAgent);
    }

    @Override
    public Agents queryById(Integer id) {
        Agents agents = agentsMapper.selectByPrimaryKey(id);
        return agents;
    }

    private void agentTrimToNull(Agents agents) {
        agents.setAddress(StringUtils.trimToNull(agents.getAddress()));
        agents.setName(StringUtils.trimToNull(agents.getName()));
        agents.setPhone(StringUtils.trimToNull(agents.getPhone()));
        agents.setTel(StringUtils.trimToNull(agents.getTel()));

    }

    @Override
    public void updateBalance(AgentsUpdatePojo agentsUpdatePojo) {
        if (agentsUpdatePojo.getId() == null){
            throw BasicException.newInstance().error("id不能为空", 500);
        }
        if (agentsUpdatePojo.getBalance() == null){
            throw BasicException.newInstance().error("余额不能为空", 500);
        }
        if (StringUtils.isBlank(agentsUpdatePojo.getPaymentType())){
            throw BasicException.newInstance().error("支付方式不能为空", 500);
        }
        if (StringUtils.isBlank(agentsUpdatePojo.getOperationType())){
            throw BasicException.newInstance().error("操作类型不能为空", 500);
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
            throw BasicException.newInstance().error("没有找到对应代理人", 500);
        }
        BigDecimal balance = agents.getBalance();
        if (agentsUpdatePojo.getOperationType().equals("add")){
            balance = balance.add(agentsUpdatePojo.getBalance());
        } else {
            if (balance.compareTo(agentsUpdatePojo.getBalance()) < 0){
                throw BasicException.newInstance().error("余额小于要扣金额，不可操作", 500);
            }
            balance = balance.subtract(agentsUpdatePojo.getBalance());
        }
        agentsMapper.updateBalance(agentsUpdatePojo.getId(), balance);
        agentPriceHistoryMapper.insert(history);
    }

    @Override
    public void addAgent(Agents agents) {
        if (StringUtils.isBlank(agents.getAddress())){
            throw BasicException.newInstance().error("地址不能为空", 500);
        }
        if (StringUtils.isBlank(agents.getName())){
            throw BasicException.newInstance().error("名字不能为空", 500);
        }
        if (StringUtils.isBlank(agents.getPhone())){
            throw BasicException.newInstance().error("手机号码不能为空", 500);
        }
        if (StringUtils.isBlank(agents.getTel())){
            throw BasicException.newInstance().error("座机号码不能为空", 500);
        }
        if (StringUtils.isBlank(agents.getType())){
            throw BasicException.newInstance().error("代理类型不能为空", 500);
        }
        List<Agents> checkAgentList = agentsMapper.queryByPhone(agents.getPhone());
        if (!CollectionUtils.isEmpty(checkAgentList)){
            throw BasicException.newInstance().error("已经存在该手机号码,不可添加该代理", 500);
        }
        Date nowDate = new Date();
        agentTrimToNull(agents);
        agents.setCreateDate(nowDate);
        agents.setUpdateDate(nowDate);
        agents.setCreator(1);
        agents.setCreatorName("1");
        agents.setUpdator(1);
        agents.setUpdatorName("1");
        agents.setBalance(BigDecimal.ZERO);
        agentsMapper.insert(agents);
    }

    @Override
    public void delAgent(String ids) {
        if (StringUtils.isBlank(ids)){
            throw BasicException.newInstance().error("id不能为空", 500);
        }
        String[] str = ids.split(",");
        List<Integer> idList = new ArrayList<>();
        for (String id : str){
            idList.add(Integer.parseInt(id));
        }
        agentsMapper.deleteAgents(idList);
    }
}
