package com.itszt.consumer;

import com.itszt.domain.User;
import com.itszt.service.UserService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Executor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.LongAdder;


@Slf4j
//@Component
//@RabbitListener(queues = "QueueDirect")
public class DirectConsumer {


    @Autowired
    private UserService userService;

    @RabbitHandler
    public void prcessMessga(@Payload User user, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        System.out.println("DirectQueue队列中监听到message进入队列,消费者开始消费处理数据User:  " + user);

        int i = userService.insertOne(user);

        if (i > 0) {
            log.info("手动提交confirm:" + tag);
            //自动确认提交
            channel.basicAck(tag, false);
        }


    }

    @RabbitHandler
    public void prcessMessga2(String user) {

        System.out.println("DirectQueue队列中监听到message进入队列,消费者开始消费处理数据User:  " + user);


    }
}
