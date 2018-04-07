package com.mszhan.redwine.manage.server.util;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by pcq on 2017/7/4.
 */
public class ResponseUtils {

	//Amazon Maximum Page Size
	public static int maximum = 5;
	
    public static ResponseVO newResponse(){
        return new ResponseVO();
    }

    public static class ResponseVO{
        private boolean success = true;
        private String message;
        private Object data;
        private Integer errorCode;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String exceptionStackTrace;

        public String getExceptionStackTrace() {
            return exceptionStackTrace;
        }

        public void setExceptionStackTrace(String exceptionStackTrace) {
            this.exceptionStackTrace = exceptionStackTrace;
        }

        public ResponseVO succeed(Object data){
            this.setSuccess(true);
            this.setData(data);
            return this;
        }

        public ResponseVO succeedWithList(Long total, List<? extends Object> rows) {
            this.setSuccess(true);
            this.setData(FastCollections.map("total", total, "rows", rows));
            return this;
        }

        public ResponseVO succeed(){
            this.setSuccess(true);
            return this;
        }

        public ResponseVO failed(BasicException e){
            return failed(this.getData(), e);
        }

        public ResponseVO failed(Object data, Exception e){
            this.setSuccess(false);
            this.setData(data);
            if (e != null) {
                this.setMessage(e.getMessage());
                if (e instanceof BasicException) {
                    BasicException basicException = (BasicException) e;
                    if (basicException.getData() != null) {
                        this.setData(basicException.getData());
                    }
                    this.setErrorCode(basicException.getErrorCode());
                    this.setExceptionStackTrace(BasicException.stackTraceToString(basicException.getOriginException()));
                }else{
                    this.setErrorCode(ResultCode.Common.INTERNAL_SERVER_ERROR.code);
                    this.setExceptionStackTrace(BasicException.stackTraceToString(e));
                }
            }
            return this;
        }

        public ResponseVO failed(Integer errorCode, String message) {
            if (errorCode == null) {
                errorCode = ResultCode.Common.UNKNOWN_ERROR.code;
            }
            this.setSuccess(false);
            this.setErrorCode(errorCode);
            this.setMessage(message);
            return  this;
        }

        public ResponseVO message(String message) {
            this.setMessage(message);
            return this;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public Integer getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(Integer errorCode) {
            this.errorCode = errorCode;
        }
    }

}
