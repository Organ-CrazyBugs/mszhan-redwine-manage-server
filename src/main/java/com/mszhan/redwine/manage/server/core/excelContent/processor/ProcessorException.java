package com.mszhan.redwine.manage.server.core.excelContent.processor;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 吴树添
 * Date: 16-7-1
 * Time: 下午6:37
 * To change this template use File | Settings | File Templates.
 */
public class ProcessorException extends RuntimeException {

        public ProcessorException(List<String> errorList, String msg){
            super(msg);
            errorList.add(msg);
        }
    public ProcessorException(String msg){
        super(msg);
    }

        public ProcessorException(){
            super("自定义异常ProcessorException");
        }
    }