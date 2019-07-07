package com.wt.wtchat.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * spring工厂类
 *
 * @author Zed
 */
@Component
public class SpringBeanFactoryUtil implements ApplicationContextAware {
    private static ApplicationContext context = null;

    public static <T> T getBean(Class<T> type) {
        return context.getBean(type);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringBeanFactoryUtil.context == null) {
            SpringBeanFactoryUtil.context = applicationContext;
        }
    }
}
