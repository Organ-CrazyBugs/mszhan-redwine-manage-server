package com.mszhan.redwine.manage.server.web.page;

import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.WarehouseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WarehousePageController {

    @Autowired
    private WarehouseMapper warehouseMapper;

    @RequestMapping("/page/warehouse/index")
    public ModelAndView index(){
        ModelAndView view = new ModelAndView("warehouse/warehouse_index");

        view.addObject("warehouses", this.warehouseMapper.fetchAllWarehouse());
        return view;
    }

    @RequestMapping("/page/warehouse/warehouse_manage")
    public ModelAndView warehouseManage(){
        ModelAndView view = new ModelAndView("warehouse/warehouse_manage");
        return view;
    }

}
