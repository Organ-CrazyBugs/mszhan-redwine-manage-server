package com.mszhan.redwine.manage.server.web.page;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 21:35 2017/12/25
 */
@Controller
public class IndexPageController {

    @GetMapping(value = "/")
    public ModelAndView indexRootDirectory() {
        return new ModelAndView("redirect:/page/index");
    }

    @GetMapping(value = "/page/index")
    public ModelAndView index() {
        ModelAndView view = new ModelAndView("index");
        return view;
    }


}
