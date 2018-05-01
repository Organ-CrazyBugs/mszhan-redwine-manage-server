package com.mszhan.redwine.manage.server.web.page;

import com.mszhan.redwine.manage.server.core.SecurityUtils;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.AgentsMapper;
import com.mszhan.redwine.manage.server.enums.AgentTypeEnum;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Agents;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 10:23 2018/4/13
 */
@Controller
public class OrderPageController {
    @Autowired
    private AgentsMapper agentsMapper;

    /**
     * 订单打印出库单
     */
    @PostMapping("/page/order/print_output")
    public ModelAndView orderPrintOutputOrder(){
        ModelAndView view = new ModelAndView("order/order_output_warehouse");

        return view;
    }

    @RequestMapping("/page/order/order_list")
    public ModelAndView index(){
        String agentType = SecurityUtils.getAuthenticationUser().getAgentType();
        Integer agentId = SecurityUtils.getAuthenticationUser().getAgentId();
        List<Map<String, Object>> agents;
        if (AgentTypeEnum.ADMIN.toString().equals(agentType)) {
            agents = agentsMapper.fetchAllAgents();
        } else {
            Agents agent = this.agentsMapper.selectByPrimaryKey(agentId);
            Map<String, Object> map = new HashMap<>();
            map.put("id", agent.getId());
            map.put("name", agent.getName());
            map.put("phone", agent.getPhone());
            agents = Arrays.asList(map);
        }
        ModelAndView view = new ModelAndView("order/order_list");
        view.addObject("agentType", agentType);
        view.addObject("agents", agents);
        return view;
    }

}
