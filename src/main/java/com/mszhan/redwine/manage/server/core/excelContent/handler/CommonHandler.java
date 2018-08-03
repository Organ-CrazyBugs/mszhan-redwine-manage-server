package com.mszhan.redwine.manage.server.core.excelContent.handler;


import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 吴树添
 * Date: 16-6-20
 * Time: 下午2:20
 * To change this template use File | Settings | File Templates.
 */
public interface CommonHandler<T> {

    public T handler(Map<String, Object> valueMap);

}
