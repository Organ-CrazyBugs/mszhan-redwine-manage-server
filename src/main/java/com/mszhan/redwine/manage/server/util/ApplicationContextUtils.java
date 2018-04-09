package com.mszhan.redwine.manage.server.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by pcq on 2017/5/31.
 */
@Component
public class ApplicationContextUtils implements ApplicationContextAware {
    public static ApplicationContext applicationContext;

    public static ApplicationContext getContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        ApplicationContextUtils.applicationContext = context;
    }

}
