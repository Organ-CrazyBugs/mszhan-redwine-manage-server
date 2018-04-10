package com.mszhan.redwine.manage.server.service.impl;

import com.mszhan.redwine.manage.server.core.BasicException;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.UserLoginMapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.UserLogin;
import com.mszhan.redwine.manage.server.service.UserLoginService;
import com.mszhan.redwine.manage.server.core.AbstractService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.Date;


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

    @Override
    public UserLogin createUserLogin(UserLogin userLogin) {
        if (userLogin.getAgentId() == null) {
            throw BasicException.newInstance().error("请指定所属代理", 500);
        }
        if (StringUtils.isBlank(userLogin.getUserName())) {
            throw BasicException.newInstance().error("用户登陆账号不能为空", 500);
        }
        if (StringUtils.isBlank(userLogin.getPassword())) {
            throw BasicException.newInstance().error("用户登陆密码不能为空", 500);
        }
        if (StringUtils.isBlank(userLogin.getPersonName())) {
            throw BasicException.newInstance().error("使用人员名称不能为空", 500);
        }

        Condition checkCon = new Condition(UserLogin.class);
        checkCon.createCriteria().andEqualTo("userName", userLogin.getUserName());
        int count = this.userLoginMapper.selectCountByCondition(checkCon);
        if (count > 0) {
            throw BasicException.newInstance().error("用户登陆账号已经存在", 500);
        }

        Md5PasswordEncoder encoder = new Md5PasswordEncoder();
        String password = encoder.encodePassword(userLogin.getPassword(), userLogin.getUserName());
        userLogin.setPassword(password);

        userLogin.setStatus(UserLoginStatus.ENABLED.toString());
        userLogin.setCreateDate(new Date());
        userLogin.setSysUser("N");
        this.userLoginMapper.insert(userLogin);

        // 清空密码返回数据
        userLogin.setPassword(null);

        return userLogin;
    }
}
