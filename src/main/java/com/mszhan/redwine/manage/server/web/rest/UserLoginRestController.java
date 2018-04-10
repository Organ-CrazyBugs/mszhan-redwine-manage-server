package com.mszhan.redwine.manage.server.web.rest;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mszhan.redwine.manage.server.core.Requests;
import com.mszhan.redwine.manage.server.core.Responses;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.UserLoginMapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.UserLogin;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.base.PaginateResult;
import com.mszhan.redwine.manage.server.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 20:34 2017/12/27
 */
@RestController
public class UserLoginRestController {

    @Autowired
    private UserLoginMapper userLoginMapper;
    @Autowired
    private UserLoginService userLoginService;

    @GetMapping(value = "/api/user_login/list")
    public Object list(HttpServletRequest request){
        Requests requests = Requests.newInstance(request);
        Integer offset = requests.getInteger("offset", 0);
        Integer limit = requests.getInteger("limit", 10);

        Integer agentId = requests.getInteger("agentId", null);
        String status = requests.getString("status", null);
        String personName = requests.getString("personName", null);
        String userName = requests.getString("userName", null);

        Page<UserLogin> page = PageHelper.offsetPage(offset, limit).doSelectPage(() -> this.userLoginMapper.fetchAllUserLogin(agentId, status, personName, userName));
        return Responses.newInstance().succeed(PaginateResult.newInstance(page.getTotal(), page));
    }

    @PostMapping(value = "/api/user_login/create")
    public Object create(@RequestBody UserLogin userLogin){
        this.userLoginService.createUserLogin(userLogin);

        return Responses.newInstance().succeed();
    }

}
