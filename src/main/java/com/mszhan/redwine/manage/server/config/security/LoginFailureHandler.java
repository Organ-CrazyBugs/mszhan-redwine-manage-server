package com.mszhan.redwine.manage.server.config.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 登陆失败响应调整, 如果Ajax访问将返回JSON, 如果普通页面则返回HTML
 * @Author: iblilife@163.com
 * @Date: 16:21 2017/12/30
 */
@Service
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    public LoginFailureHandler(){
        this.setDefaultFailureUrl(WebSecurityConfig.loginFailureUrl);
        this.setRedirectStrategy(new BasicRedirectStrategy(false, "用户名或者密码不正确", null));
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // TODO: 处理用户登陆失败, 如记录日志等等
        super.onAuthenticationFailure(request, response, exception);
    }
}
