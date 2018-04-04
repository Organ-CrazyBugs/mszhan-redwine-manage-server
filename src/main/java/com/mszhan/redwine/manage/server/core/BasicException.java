package com.mszhan.redwine.manage.server.core;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @Description: 自定义异常类
 * @Author: iblilife@163.com
 * @Date: 22:14 2017/12/29
 */
public class BasicException extends RuntimeException{

    public static BasicException newInstance(){
        return new BasicException();
    }

    public BasicException error(String message, Integer code) {
        this.code = code;
        this.message = message;
        return this;
    }

    public BasicException originEx(Throwable originException) {
        this.originException = originException;
        return this;
    }

    public BasicException data(Object data) {
        this.data = data;
        return this;
    }

    private Integer code;
    private String message;
    private Object data;
    private Throwable originException;

    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public Throwable getOriginException() {
        return originException;
    }

    public String fetchExceptionTrace(){
        if (originException != null) {
            return BasicException.exceptionTrace(originException);
        }
        return BasicException.exceptionTrace(this);
    }

    public static String exceptionTrace(Throwable e){
        if (e == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        try (
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                PrintStream writer = new PrintStream(out);
        ){
            if (e != null) {
                e.printStackTrace(writer);
                writer.flush();
                stringBuilder.append(new String(out.toByteArray()));
            }
        } catch (Exception e1) {
        }
        return stringBuilder.toString();
    }
}
