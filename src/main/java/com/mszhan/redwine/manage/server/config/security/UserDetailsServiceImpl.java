package com.mszhan.redwine.manage.server.config.security;

import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.UserLoginMapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.UserLogin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 22:40 2017/12/24
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserLoginMapper userLoginMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        if (StringUtils.isBlank(userName)) {
            throw new UsernameNotFoundException("UserName is blank.");
        }
        Condition fetchUserInfo = new Condition(UserLogin.class);
        fetchUserInfo.createCriteria().andEqualTo("userName", userName);
        List<UserLogin> userLogins = this.userLoginMapper.selectByCondition(fetchUserInfo);
        if (CollectionUtils.isEmpty(userLogins)) {
            throw new UsernameNotFoundException("UserName [" + userName + "] is not found.");
        }
        UserLogin userLogin = userLogins.get(0);

        //TODO: 获取角色信息
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        return User.withUsername(userName)
                .password(userLogin.getPassword())
                .accountExpired(false)
                .accountLocked(false)
                .disabled(false)
                .authorities(authorities)
                .credentialsExpired(false)
                .build();
    }

}
