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
import org.springframework.util.Assert;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


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
        Assert.notNull(userLogin, "缺少参数内容");
        Assert.notNull(userLogin.getAgentId(), "请指定所属代理");
        Assert.hasLength(userLogin.getUserName(), "用户登陆账号不能为空");
        Assert.hasLength(userLogin.getPassword(), "用户登陆密码不能为空");
        Assert.hasLength(userLogin.getPersonName(), "使用人员名称不能为空");

        Condition checkCon = new Condition(UserLogin.class);
        checkCon.createCriteria().andEqualTo("userName", userLogin.getUserName());
        int count = this.userLoginMapper.selectCountByCondition(checkCon);
        Assert.isTrue(count == 0, "用户登陆账号已经存在");

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

    @Override
    public void changeStatus(List<Integer> ids, UserLoginStatus status) {
        Assert.notNull(status, "状态参数不能为空");
        Assert.notEmpty(ids, "需要更改状态的登陆账号ID不能为空");

        Condition updateCon = new Condition(UserLogin.class);
        updateCon.createCriteria().andNotEqualTo("userName", "admin") // admin 系统管理员用户无法禁用
                .andIn("id", ids);

        UserLogin userLogin = new UserLogin();
        userLogin.setStatus(status.toString());
        this.userLoginMapper.updateByConditionSelective(userLogin, updateCon);
    }
}
