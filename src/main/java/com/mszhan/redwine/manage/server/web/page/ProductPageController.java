package com.mszhan.redwine.manage.server.web.page;

import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 11:21 2018/4/10
 */
@Controller
public class ProductPageController {



    @GetMapping("/page/product/index")
    public ModelAndView index() {
        ModelAndView view = new ModelAndView("product/product_list");
        return view;
    }

    @GetMapping("/page/product/create_index")
    public ModelAndView createIndex() {
        ModelAndView view = new ModelAndView("product/create_product");
        return view;
    }

    @GetMapping("/page/product/edit_index")
    public ModelAndView editIndex(Integer id) {
        ModelAndView view = new ModelAndView("product/edit_product");
        view.addObject("id", id);
        return view;
    }

    @GetMapping("/page/product/product_detail_index")
    public ModelAndView productDetailIndex(Integer id) {
        ModelAndView view = new ModelAndView("product/show_product_detail_info");
        view.addObject("id", id);
        return view;
    }


}
