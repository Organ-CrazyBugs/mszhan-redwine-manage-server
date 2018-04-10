package com.mszhan.redwine.manage.server.web;

import com.mszhan.redwine.manage.server.core.Responses;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Agents;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.base.PaginateResult;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.AgentQuery;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.query.AgentsUpdatePojo;
import com.mszhan.redwine.manage.server.service.AgentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
    public Object search(Integer id) {
        return Responses.newInstance().succeed(agentsService.queryById(id));
    }

    @GetMapping(value = "/search")
    public Object search(AgentQuery query) {
        PaginateResult<Agents> result = agentsService.queryForPage(query);
        return Responses.newInstance().succeed(result);
    }

    @PostMapping(value = "/add_agent")
    public Object addAgents(@RequestBody Agents agents) {
        agentsService.addAgent(agents);
        return Responses.newInstance().succeed();
    }

    @PutMapping(value = "/update_balance")
    public Object updateBalance(@RequestBody AgentsUpdatePojo agentsUpdatePojo) {
        agentsService.updateBalance(agentsUpdatePojo);
        return Responses.newInstance().succeed();
    }

    @PutMapping(value = "/update_agent")
    public Object updateAgents(@RequestBody Agents agents) {
        agentsService.updateAgent(agents);
        return Responses.newInstance().succeed();
    }

    @DeleteMapping(value = "/delete_by_ids/{ids}")
    public Object deleteById(@PathVariable("ids") String ids) {
        agentsService.delAgent(ids);
        return Responses.newInstance().succeed();
    }



}
