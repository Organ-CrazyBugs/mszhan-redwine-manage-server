package com.mszhan.redwine.manage.server.web.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 22:19 2017/12/25
 */
@RestController
@RequestMapping("/")
public class ClientPageController {

    @GetMapping(value = "/page/client/index")
    public ModelAndView index(){
        ModelAndView view = new ModelAndView("client/client");
        return view;
    }


}
