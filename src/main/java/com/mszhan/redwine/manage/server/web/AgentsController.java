package com.mszhan.redwine.manage.server.web;

import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Agents;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.AgentQuery;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.AgentsUpdatePojo;
import com.mszhan.redwine.manage.server.service.AgentsService;
import com.mszhan.redwine.manage.server.service.ProductService;
import com.mszhan.redwine.manage.server.util.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import sun.management.Agent;

@RestController
@RequestMapping("/agent")
public class AgentsController {

    @Autowired
    private AgentsService agentsService;
    @GetMapping(value = "/index")
    public ModelAndView productIndex() {
        ModelAndView view = new ModelAndView("agent/agent");
        return view;
    }
    @GetMapping(value = "/query_by_id")
    public ResponseUtils.ResponseVO search(Integer id) {
        return agentsService.queryById(id);
    }


    @GetMapping(value = "/search")
    public ResponseUtils.ResponseVO search(AgentQuery query) {
        return agentsService.queryForPage(query);
    }

    @PostMapping(value = "/add_agent")
    public ResponseUtils.ResponseVO addAgents(@RequestBody Agents agents) {
        return agentsService.addAgent(agents);
    }

    @PutMapping(value = "/update_balance")
    public ResponseUtils.ResponseVO updateBalance(@RequestBody AgentsUpdatePojo agentsUpdatePojo) {
        return agentsService.updateBalance(agentsUpdatePojo);
    }

    @PutMapping(value = "/update_agent")
    public ResponseUtils.ResponseVO updateAgents(@RequestBody Agents agents) {
        return agentsService.updateAgent(agents);
    }

    @DeleteMapping(value = "/delete_by_id/{id}")
    public ResponseUtils.ResponseVO deleteById(@PathVariable("id") Integer id) {
        return agentsService.delAgent(id);
    }



}
