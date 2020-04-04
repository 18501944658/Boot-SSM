package com.itszt.config;

import com.itszt.domain.User;
import org.aopalliance.aop.Advice;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.util.List;

@Configuration
public class RabbitMqConfig {


    /**
     * 对消息队列中数据箭监听,以及一些基础配置
     *
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setMessageConverter(new MessageConverter() {
            @Override
            public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
                return null;
            }

            @Override
            public Object fromMessage(Message message) throws MessageConversionException {
                System.out.println("enter rabbitListenerContainerFactory");

                try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(message.getBody()))) {
                    return (User) ois.readObject();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        });
        return factory;
    }


    @Bean
    public RabbitTemplate createRabbitmq(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        //设置消息从生产者到队列无接受则返回给生产者true.fasle为直接丢弃
        rabbitTemplate.setMandatory(true);
        //消息主体进行字节转实体
//        rabbitTemplate.setMessageConverter(new MessageConverter() {
//            @Override
//            public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
//                return null;
//            }
//
//            @Override
//            public Object fromMessage(Message message) throws MessageConversionException {
//                try {
//                    ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(message.getBody()));
//                    return (User) objectInputStream.readObject();
//                } catch (IOException e) {
//                    e.printStackTrace();
//
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//
//                }
//                return null;
//            }
//        });

        //设置从生产者到交换器的回调,确认到达后的回调

        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean b, String s) {
                System.out.println("correlationData =  相关数据" + correlationData);

                System.out.println("ack = 确认情况" + b);

                System.out.println("ConfirmCallback =  原因 " + s);

                System.out.println("correlationData =  id" + correlationData.getId());
            }
        });
        //路由不到队列时触发回调
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int i, String s, String exchange, String routing) {
                System.out.println("消息主体 message : " + message);
                System.out.println("消息主体 message : " + i);
                System.out.println("描述：" + s);
                System.out.println("消息使用的交换器 exchange : " + exchange);
                System.out.println("消息使用的路由键 routing : " + routing);

//                rabbitTemplate.convertAndSend(exchange, routing, message);

            }
        });


        return rabbitTemplate;


    }

}
