package com.mszhan.redwine.manage.server.config.security;

import com.mszhan.redwine.manage.server.core.Requests;
import com.mszhan.redwine.manage.server.core.Responses;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 处理 登陆/退出 后页面跳转, AJAX访问不跳转界面而是返回JSON数据, 普通页面登陆则跳转至页面
 * @Author: iblilife@163.com
 * @Date: 14:42 2017/12/30
 */
public class BasicRedirectStrategy implements RedirectStrategy {

    private boolean succeed;
    private String message;
    private Integer errorCode;
    private User user;

    public BasicRedirectStrategy(boolean succeed) {
        this(succeed, null, null);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BasicRedirectStrategy(boolean succeed, String message, Integer errorCode) {
        this.succeed = succeed;
        this.errorCode = errorCode;
        this.message = message;
    }

    // 用户处理普通页面登陆跳转
    private RedirectStrategy defaultRedirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url) throws IOException {
        Requests requests = Requests.newInstance(request);
        if (requests.isAjax()) {
            Responses responses = Responses.newInstance();
            if (succeed) {
                Map<String, Object> data = new HashMap<>();
                data.put("userName", user == null ? "" : user.getUsername());
                responses.succeed(data);
            } else {
                responses.failed(message, errorCode);
            }
            responses.response(response).writeAsJson(HttpStatus.OK.value());
        } else {
            this.defaultRedirectStrategy.sendRedirect(request, response, url);
        }
    }
}