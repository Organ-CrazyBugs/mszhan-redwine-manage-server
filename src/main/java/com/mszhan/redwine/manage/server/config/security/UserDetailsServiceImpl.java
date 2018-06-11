package com.mszhan.redwine.manage.server.config.security;

import com.mszhan.redwine.manage.server.core.BasicException;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.AgentsMapper;
import com.mszhan.redwine.manage.server.dao.mszhanRedwineManage.UserLoginMapper;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.Agents;
import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.UserLogin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    @Autowired
    private AgentsMapper agentsMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        if (StringUtils.isBlank(userName)) {
            throw new UsernameNotFoundException("UserName is blank.");
        }

<<<<<<< HEAD

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        if (Integer.valueOf(dateFormat.format(new Date())) >= 20180618) {
            throw BasicException.newInstance().error("有效期超时，请联系开发人员", 500);
        }
=======
        /*
            限制使用时间
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            if (Integer.valueOf(dateFormat.format(new Date())) >= 20180618) {
                throw BasicException.newInstance().error("有效期超时，请联系开发人员", 500);
            }
        */
>>>>>>> ce3468f00a624c0864cf9d9cdad431ab5f2071c7


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

        // 获取代理信息
        Integer agentId = userLogin.getAgentId();
        String agentName = null;
        String agentType = null;
        if (agentId != null) {
            Agents agent = this.agentsMapper.selectByPrimaryKey(agentId);
            if (agent != null) {
                agentName = agent.getName();
                agentType = agent.getType();
            }
        }

        return User.withUserLogin(userLogin)
                .accountExpired(false)
                .accountLocked(false)
                .disabled(false)
                .authorities(authorities)
                .credentialsExpired(false)
                .agentName(agentName)
                .agentId(agentId)
                .agentType(agentType)
                .sysUser(userLogin.getSysUser())
                .build()
                ;
    }

}
