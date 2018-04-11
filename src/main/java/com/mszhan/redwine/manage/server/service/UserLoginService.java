package com.mszhan.redwine.manage.server.service;

import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.UserLogin;
import com.mszhan.redwine.manage.server.core.Service;

import java.util.List;

/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 21:50 2018/04/06
 */
public interface UserLoginService extends Service<UserLogin> {

    enum UserLoginStatus {
        ENABLED,DISABLED;
    }

    UserLogin createUserLogin(UserLogin userLogin);

    void changeStatus(List<Integer> ids, UserLoginStatus status);

}
