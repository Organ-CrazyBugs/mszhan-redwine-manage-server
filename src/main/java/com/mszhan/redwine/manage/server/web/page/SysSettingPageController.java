package com.mszhan.redwine.manage.server.web.page;

import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.AgentsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 15:14 2018/4/10
 */
@Controller
public class SysSettingPageController {

    @Autowired
    private AgentsMapper agentsMapper;

    @GetMapping(value = "/page/sys_setting/login_account_list")
    public ModelAndView loginAccountList(){
        ModelAndView view = new ModelAndView("sys_setting/login_account_list");
        view.addObject("agents", agentsMapper.fetchAllAgents());
        return view;
    }

}
