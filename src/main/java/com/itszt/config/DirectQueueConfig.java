package com.itszt.config;


import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DirectQueueConfig {

    /**
     * 我们声明一个队列DirQueue
     *
     * @return
     */
    @Bean
    public Queue DirQueue() {
        return new Queue("QueueDirect",true);
    }


    /**
     * 声明交换器的类型
     *
     * @return
     */

    @Bean
    public DirectExchange DirExchange() {
        return new DirectExchange("ExchangeDirect");
    }


    /**
     * 声明Bingdingkey的绑定
     *
     * @return
     */
    @Bean
    public Binding Binging() {
        return BindingBuilder.bind(DirQueue()).to(DirExchange()).with("directing");
    }

}
