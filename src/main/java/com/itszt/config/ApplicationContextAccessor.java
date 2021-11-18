package com.itszt.config;

import org.springframework.context.ApplicationContext;

/**
 * 根据beanname获取bean实体
 */
public class ApplicationContextAccessor{


    private static ApplicationContext applicationContext;


    public ApplicationContextAccessor(ApplicationContext applicationContext) {
        ApplicationContextAccessor.applicationContext = applicationContext;
    }

//    @Override
//    public void setApplicationContext(ApplicationContext context) throws BeansException {
//        System.out.println("=======================================" + context + "=============================================");
//        ApplicationContextAccessor.applicationContext = context;
//    }

    public static <T> T getBean(String beanName) {
        return (T) applicationContext.getBean(beanName);
    }

    public static <T> T getBean(Class<T> beanClass) {
        return applicationContext.getBean(beanClass);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }


}
