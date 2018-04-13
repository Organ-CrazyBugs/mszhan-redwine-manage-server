package com.mszhan.redwine.manage.server.web.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 10:23 2018/4/13
 */
@Controller
public class OrderPageController {
    @RequestMapping("/page/order/order_list")
    public ModelAndView index(){
        ModelAndView view = new ModelAndView("order/order_list");
        return view;
    }

}
