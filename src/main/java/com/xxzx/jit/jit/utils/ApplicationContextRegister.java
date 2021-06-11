package com.xxzx.jit.jit.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextRegister implements ApplicationContextAware {
    private static ApplicationContext APPLICATION_CONTEXT;
    /**
     * 设置spring上下文
     * @param applicationContext spring上下文
     * @throws BeansException
     * */
    @Override  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        APPLICATION_CONTEXT = applicationContext;
    }

    /**
     * 获取容器
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }

    /**
     * 获取容器对象
     * @param type
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> type) {
        return APPLICATION_CONTEXT.getBean(type);
    }

    public static <T> T getBean(String name,Class<T> clazz){
        return APPLICATION_CONTEXT.getBean(name, clazz);
    }

    public static Object getBean(String name){
        return APPLICATION_CONTEXT.getBean(name);
    }
}
