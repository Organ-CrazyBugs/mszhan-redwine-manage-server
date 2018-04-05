package com.mszhan.redwine.manage.server.config.security;

import com.mszhan.redwine.manage.server.core.Requests;
import com.mszhan.redwine.manage.server.core.Responses;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @Description: 处理Ajax访问/api/**类借口时未登录返回JSON数据, 而不是返回登录界面的html内容
 * @Author: iblilife@163.com
 * @Date: 14:37 2017/12/29
 */
@Component
public class UserLoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        Requests requests = Requests.newInstance(request);
        String path = request.getServletPath();
        if ( StringUtils.isNotBlank(path)
             && requests.isAjax()
             && !path.startsWith(WebSecurityConfig.loginProcessingUrl) // 排除登录接口不作处理
            ) {
            HttpSession session = request.getSession();
            SecurityContextImpl securityContext = (SecurityContextImpl) session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
            if (securityContext == null || securityContext.getAuthentication() == null) {
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                Responses responses = Responses.newInstance().failed(HttpStatus.UNAUTHORIZED.getReasonPhrase(), HttpStatus.UNAUTHORIZED.value());
                responses.response(response).writeAsJson(HttpStatus.UNAUTHORIZED.value());
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
