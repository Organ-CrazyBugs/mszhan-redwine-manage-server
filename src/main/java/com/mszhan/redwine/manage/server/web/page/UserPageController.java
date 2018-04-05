package com.mszhan.redwine.manage.server.web.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 22:19 2017/12/25
 */
@Controller
public class UserPageController {

    @GetMapping(value = "/page/login")
    public ModelAndView login(){
        ModelAndView view = new ModelAndView("login");
        return view;
    }

}
