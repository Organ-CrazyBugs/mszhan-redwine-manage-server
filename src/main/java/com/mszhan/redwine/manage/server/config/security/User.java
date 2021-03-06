package com.mszhan.redwine.manage.server.config.security;

import com.mszhan.redwine.manage.server.model.mszhanRedwineManage.UserLogin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 15:59 2018/4/10
 */
public class User extends org.springframework.security.core.userdetails.User {
    public User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public User(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    private Integer userLoginId;
    private Integer agentId;
    private String agentName;
    private String agentType;
    private String sysUser;

    public static UserBuilder withUserLogin(UserLogin userLogin) {
        return new UserBuilder()
                .username(userLogin.getUserName())
                .userLoginId(userLogin.getId())
                .password(userLogin.getPassword())
                ;
    }

    public static class UserBuilder {
        private String username;
        private Integer userLoginId;
        private String password;
        private List<GrantedAuthority> authorities;
        private boolean accountExpired;
        private boolean accountLocked;
        private boolean credentialsExpired;
        private boolean disabled;
        private Integer agentId;
        private String agentName;
        private String agentType;
        private String sysUser;

        public String getSysUser() {
            return sysUser;
        }

        public UserBuilder sysUser(String sysUser) {
            this.sysUser = sysUser;
            return this;
        }

        /**
         * Creates a new instance
         */
        private UserBuilder() {
        }

        public UserBuilder agentId(Integer agentId) {
            this.agentId = agentId;
            return this;
        }

        public UserBuilder agentName(String agentName) {
            this.agentName = agentName;
            return this;
        }

        public UserBuilder agentType(String agentType) {
            this.agentType = agentType;
            return this;
        }

        private UserBuilder username(String username) {
            Assert.notNull(username, "username cannot be null");
            this.username = username;
            return this;
        }

        public UserBuilder userLoginId(Integer userLoginId){
            this.userLoginId = userLoginId;
            return this;
        }

        public UserBuilder password(String password) {
            Assert.notNull(password, "password cannot be null");
            this.password = password;
            return this;
        }

        public UserBuilder roles(String... roles) {
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(
                    roles.length);
            for (String role : roles) {
                Assert.isTrue(!role.startsWith("ROLE_"), role
                        + " cannot start with ROLE_ (it is automatically added)");
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
            return authorities(authorities);
        }

        public UserBuilder authorities(GrantedAuthority... authorities) {
            return authorities(Arrays.asList(authorities));
        }

        public UserBuilder authorities(List<? extends GrantedAuthority> authorities) {
            this.authorities = new ArrayList<GrantedAuthority>(authorities);
            return this;
        }

        public UserBuilder authorities(String... authorities) {
            return authorities(AuthorityUtils.createAuthorityList(authorities));
        }

        public UserBuilder accountExpired(boolean accountExpired) {
            this.accountExpired = accountExpired;
            return this;
        }

        public UserBuilder accountLocked(boolean accountLocked) {
            this.accountLocked = accountLocked;
            return this;
        }

        public UserBuilder credentialsExpired(boolean credentialsExpired) {
            this.credentialsExpired = credentialsExpired;
            return this;
        }

        public UserBuilder disabled(boolean disabled) {
            this.disabled = disabled;
            return this;
        }

        public UserDetails build() {
            User user = new User(username, password, !disabled, !accountExpired, !credentialsExpired, !accountLocked, authorities);
            user.setUserLoginId(userLoginId);
            user.setAgentId(agentId);
            user.setAgentName(agentName);
            user.setAgentType(agentType);
            user.setSysUser(sysUser);
            return user;
        }
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public Integer getUserLoginId() {
        return userLoginId;
    }

    public void setUserLoginId(Integer userLoginId) {
        this.userLoginId = userLoginId;
    }

    public String getAgentType() {
        return agentType;
    }

    public void setAgentType(String agentType) {
        this.agentType = agentType;
    }

    public String getSysUser() {
        return sysUser;
    }

    public void setSysUser(String sysUser) {
        this.sysUser = sysUser;
    }
}
