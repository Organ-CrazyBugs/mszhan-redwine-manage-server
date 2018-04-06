package com.mszhan.redwine.manage.server.service.impl;

import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.UserLoginMapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.UserLogin;
import com.mszhan.redwine.manage.server.service.UserLoginService;
import com.mszhan.redwine.manage.server.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 21:50 2018/04/06
 */
@Service
@Transactional
public class UserLoginServiceImpl extends AbstractService<UserLogin> implements UserLoginService {
    @Resource
    private UserLoginMapper userLoginMapper;

}
