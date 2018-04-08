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

@RestController
@RequestMapping("/agents")
public class AgentsController {

    @Autowired
    private AgentsService agentsService;
    @GetMapping(value = "/index")
    public ModelAndView productIndex() {
        ModelAndView view = new ModelAndView("agents");
        return view;
    }

    @GetMapping(value = "/search")
    public ResponseUtils.ResponseVO search(AgentQuery query) {
        return agentsService.queryForPage(query);
    }
    @PostMapping(value = "/add_agents")
    public ResponseUtils.ResponseVO addAgents(@RequestBody AgentsUpdatePojo agentsUpdatePojo) {
        return agentsService.updateBalance(agentsUpdatePojo);
    }

    @PutMapping(value = "/update_balance")
    public ResponseUtils.ResponseVO updateBalance(@RequestBody AgentsUpdatePojo agentsUpdatePojo) {
        return agentsService.updateBalance(agentsUpdatePojo);
    }

    @PutMapping(value = "/update_agents")
    public ResponseUtils.ResponseVO updateAgents(@RequestBody Agents agents) {
        return agentsService.updateAgent(agents);
    }

    @DeleteMapping(value = "/delete_by_id/{id}")
    public ResponseUtils.ResponseVO deleteById(@PathVariable("id") Integer id) {
        return agentsService.delAgent(id);
    }



}
