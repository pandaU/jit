package com.xxzx.jit.jit.utils;

import com.xxzx.jit.jit.TestController;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

public class RegisterBean {
    public static Object registerBean(String name, Class cl) {
        //将applicationContext转换为ConfigurableApplicationContext
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) ApplicationContextRegister.getApplicationContext();

        // 获取bean工厂并转换为DefaultListableBeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();

        // 通过BeanDefinitionBuilder创建bean定义
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(cl);

        // 设置属性userService,此属性引用已经定义的bean:userService,这里userService已经被spring容器管理了.
        //beanDefinitionBuilder.addPropertyReference("testService", "testService");

        // 注册bean
        try {
            defaultListableBeanFactory.registerBeanDefinition(name, beanDefinitionBuilder.getRawBeanDefinition());
        }catch (Exception e){
            System.out.println("bean 已经注册");
        }
        Object bean =  ApplicationContextRegister.getBean(name);

        return bean;

        //删除bean.
        //defaultListableBeanFactory.removeBeanDefinition("testService");
    }

    public static void controlCenter(Class<?> controllerClass, ApplicationContext Context, Integer type,String methodName,String apiMapping) throws IllegalAccessException, Exception{
        //获取RequestMappingHandlerMapping
        RequestMappingHandlerMapping requestMappingHandlerMapping=(RequestMappingHandlerMapping) Context.getBean(RequestMappingHandlerMapping.class);
        Method getMappingForMethod = ReflectionUtils.findMethod(RequestMappingHandlerMapping.class, "getMappingForMethod",Method.class,Class.class);
        //设置私有属性为可见
        getMappingForMethod.setAccessible(true);
        //获取类中的方法
        Method[] method_arr = controllerClass.getMethods();
        for (Method method : method_arr) {
            //判断方法上是否有注解RequestMapping
            String name = method.getName();
            if (name.equals(methodName)) {
                //获取到类的RequestMappingInfo
                RequestMappingInfo mappingInfo = (RequestMappingInfo) getMappingForMethod.invoke(requestMappingHandlerMapping, method,controllerClass);
                mappingInfo = RequestMappingInfo.paths(apiMapping).build().combine(mappingInfo);
                if(type == 1){
                    //注册
                    registerMapping(requestMappingHandlerMapping, mappingInfo, controllerClass, method);
                }else if(type == 2){
                    //取消注册
                    unRegisterMapping(requestMappingHandlerMapping, mappingInfo);
                    registerMapping(requestMappingHandlerMapping, mappingInfo, controllerClass, method);
                }else if(type == 3){
                    unRegisterMapping(requestMappingHandlerMapping, mappingInfo);
                }

            }
        }
    }

    /**
     *
     * registerMapping(注册mapping到spring容器中)
     * @param   requestMappingHandlerMapping
     * @Exception 异常对象
     * @since  CodingExample　Ver(编码范例查看) 1.1
     * @author jiaxiaoxian
     */
    public static void registerMapping(RequestMappingHandlerMapping requestMappingHandlerMapping,RequestMappingInfo mappingInfo, Class<?> controllerClass, Method method) throws Exception, IllegalAccessException{
        requestMappingHandlerMapping.registerMapping(mappingInfo, ApplicationContextRegister.getBean(controllerClass),method);
    }

    /**
     *
     * unRegisterMapping(spring容器中删除mapping)
     * @param   requestMappingHandlerMapping
     * @Exception 异常对象
     * @since  CodingExample　Ver(编码范例查看) 1.1
     * @author jiaxiaoxian
     */
    public static void unRegisterMapping(RequestMappingHandlerMapping requestMappingHandlerMapping,RequestMappingInfo mappingInfo) throws Exception, IllegalAccessException{
        requestMappingHandlerMapping.unregisterMapping(mappingInfo);
    }
}
