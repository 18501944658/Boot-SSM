//package com.itszt.config;
//
//import com.itszt.consumer.DirectRecive;
//import com.itszt.service.UserService;
//import org.springframework.amqp.core.AcknowledgeMode;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class MessageConsumer {
//
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//
//
//
//    @Autowired
//    private DirectQueueConfig directQueueConfig;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private DirectRecive directRecive;
//
//    @Bean
//    public SimpleMessageListenerContainer messageListenerContainer() {
//        SimpleMessageListenerContainer simple= new SimpleMessageListenerContainer();
//        simple.setConnectionFactory(rabbitTemplate.getConnectionFactory());
//        simple.setQueues(directQueueConfig.DirQueue());
//        simple.setConcurrentConsumers(1);
//        simple.setMaxConcurrentConsumers(5);
//        simple.setAcknowledgeMode(AcknowledgeMode.MANUAL);
//        simple.setMessageListener(directRecive);
//        return simple;
//    }
//}
