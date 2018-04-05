package com.mszhan.redwine.manage.server.web.page;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WarehousePageController {

    @RequestMapping("/page/warehouse/index")
    public ModelAndView index(){
        ModelAndView view = new ModelAndView("warehouse/warehouse_index");
        return view;
    }

}
