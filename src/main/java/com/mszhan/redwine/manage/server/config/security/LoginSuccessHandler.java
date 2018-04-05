package com.mszhan.redwine.manage.server.config.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description:
 * @Author: iblilife@163.com
 * @Date: 23:45 2017/12/24
 */
@Service
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private BasicRedirectStrategy strategy;

    public LoginSuccessHandler() {
        strategy = new BasicRedirectStrategy(true);
        this.setRedirectStrategy(strategy);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            this.strategy.setUser(user);
        }
        //TODO: 记录用户登录信息
        super.onAuthenticationSuccess(request, response, authentication);
    }

}
