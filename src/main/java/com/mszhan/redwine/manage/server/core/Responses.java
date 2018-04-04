package com.mszhan.redwine.manage.server.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 用于处理Ajax返回数据
 * @Author: iblilife@163.com
 * @Date: 23:04 2017/12/28
 */
public class Responses {
    private boolean success = true;
    private Object data;
    private Error error;

    public static Responses newInstance() {
        return new Responses();
    }

    public Responses failed(String message, Integer code, String trace){
        this.success = false;
        this.error = new Error(code, message, trace);
        return this;
    }

    public Responses failed(String message, Integer code) {
        return failed(message, code, null);
    }

    public Responses failed(BasicException e) {
        return failed(e.getMessage(), e.getCode(), e.fetchExceptionTrace()).data(e.getData());
    }

    public Responses data(Object data) {
        this.data = data;
        return this;
    }

    public Responses succeed(){
        this.success = true;
        return this;
    }

    public Responses succeed(Object data){
        this.data = data;
        this.success = true;
        return this;
    }

    public Responses exportExcel07Init(String fileName){
        response.reset();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        try {
            response.setHeader("Content-Disposition", "attachment;filename="+ new String((fileName + ".xlsx").getBytes(), "iso-8859-1"));
        } catch (UnsupportedEncodingException e) {
            throw BasicException.newInstance().error(e.getMessage(), 500).originEx(e);
        }
        return this;
    }

    public Responses exportCSVInit(String fileName){
        response.reset();
        response.setContentType("application/csv");
        try {
            response.setHeader("Content-Disposition", "attachment;filename="+ new String((fileName + ".csv").getBytes(), "iso-8859-1"));
        } catch (UnsupportedEncodingException e) {
            throw BasicException.newInstance().error(e.getMessage(), 500).originEx(e);
        }
        return this;
    }

    /**
     * 错误信息
     */
    public static class Error{
        private Integer code;
        private String message;
        private String trace;

        public Error(Integer code, String message, String trace) {
            this.code = code;
            this.message = message;
            this.trace = trace;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getTrace() {
            return trace;
        }

        public void setTrace(String trace) {
            this.trace = trace;
        }
    }

    @JsonIgnore
    private HttpServletResponse response;

    public Responses response(HttpServletResponse response){
        this.response = response;
        return this;
    }

    public void writeAsJson(int httpStatus) throws IOException {
        if (response == null) {
            throw BasicException.newInstance().error("HttpServletResponse is Null", 500);
        }
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setStatus(httpStatus);

        ObjectMapper jsonMapper = new ObjectMapper();
        String result = jsonMapper.writeValueAsString(this);
        response.getWriter().write(result);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
