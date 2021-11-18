package com.itszt.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

   /**@Bean等同于直接引用另一个类中FactroyMethodBean中非静态FactoryMethod方法**/
    @Bean("applicationContextAccessor")
    public ApplicationContextAccessor getApplicationContextAccessor(ApplicationContext applicationContext){
        return new ApplicationContextAccessor(applicationContext);
    }
}
