package com.mszhan.redwine.manage.server.web.page;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by god on 2018/4/10.
 */
@RestController
@RequestMapping("/")
public class AgentPageController {

    @GetMapping(value = "/page/agent/index")
    public ModelAndView productIndex() {
        ModelAndView view = new ModelAndView("agent/agent");
        return view;
    }
}
