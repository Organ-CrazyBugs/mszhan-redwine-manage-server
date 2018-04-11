package com.mszhan.redwine.manage.server.config;

import com.mszhan.redwine.manage.server.core.BasicException;
import com.mszhan.redwine.manage.server.core.Requests;
import com.mszhan.redwine.manage.server.core.Responses;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Description: 统一异常处理
 * @Author: iblilife@163.com
 * @Date: 21:58 2017/12/28
 */
@Controller
@RequestMapping("${server.error.path:/error}")
public class WebBasicErrorController extends AbstractErrorController {

    private final ErrorProperties errorProperties;
    private final ErrorAttributes errorAttributes;

    @Autowired
    public WebBasicErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties) {
        this(errorAttributes, serverProperties.getError(), Collections.emptyList());
    }

    public WebBasicErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties, List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, errorViewResolvers);
        Assert.notNull(errorProperties, "ErrorProperties must not be null");
        this.errorProperties = errorProperties;
        this.errorAttributes = errorAttributes;
    }

    @Override
    public String getErrorPath() {
        return errorProperties.getPath();
    }

    @RequestMapping
    public ModelAndView error(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = getStatus(request);
        Map<String, Object> model = Collections.unmodifiableMap( getErrorAttributes(request, isIncludeStackTrace(request, null)));

        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        Throwable error = this.errorAttributes.getError(requestAttributes);
        String path = (String) model.get("path");
        Requests requests = Requests.newInstance(request);
        if ((StringUtils.isNotBlank(path) && path.startsWith("/api/")) || requests.isAjax()) {
            // API返回JSON数据
            String message = (String) model.get("message");
            String trace = (String) model.get("trace");
            Integer code = null;
            Object data = null;
            if (error != null && error instanceof BasicException) {
                BasicException basicException = (BasicException) error;
                data = basicException.getData();
                code = basicException.getCode();
            }
            Responses responses = Responses.newInstance().failed(message, code, trace).data(data);
            try {
                responses.response(response).writeAsJson(HttpStatus.OK.value());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            // 其他URL跳转至error页面
            response.setStatus(status.value());
            ModelAndView modelAndView = resolveErrorView(request, response, status, model);
            return (modelAndView == null ? new ModelAndView("error", model) : modelAndView);
        }
    }

    /*@RequestMapping(consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<Responses> error(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
        HttpStatus status = getStatus(request);
        String message = (String) body.get("message");
        String trace = (String) body.get("trace");
        Responses failed = Responses.newInstance()
                .failed(message, status.value(), trace);
        return new ResponseEntity<>(failed, status);
    }*/

    protected boolean isIncludeStackTrace(HttpServletRequest request, MediaType produces) {
        ErrorProperties.IncludeStacktrace include = getErrorProperties().getIncludeStacktrace();
        if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
            return true;
        }
        if (include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM) {
            return getTraceParameter(request);
        }
        return false;
    }

    public ErrorProperties getErrorProperties() {
        return errorProperties;
    }


}
