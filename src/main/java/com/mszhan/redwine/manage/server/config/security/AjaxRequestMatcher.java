package com.mszhan.redwine.manage.server.config.security;

import com.mszhan.redwine.manage.server.core.Requests;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

public class AjaxRequestMatcher implements RequestMatcher {
    @Override
    public boolean matches(HttpServletRequest request) {
        return Requests.newInstance(request).isAjax();
    }
}
