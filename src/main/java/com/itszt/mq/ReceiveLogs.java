package com.itszt.mq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogs {

    private static final String EXANGE_NAME = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost("192.168.0.144");

        Connection connection = connectionFactory.newConnection();

        Channel channel = connection.createChannel();


        channel.exchangeDeclare(EXANGE_NAME, "fanout");
        //无参声明一个独占队列,不持久化,断开连接自动删除的队列,getQueue()方法返回声明队列名
        String queueName = channel.queueDeclare().getQueue();
        //
        channel.queueBind(queueName, EXANGE_NAME, "");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");


        Consumer consumer = new DefaultConsumer(channel);

        channel.basicConsume(queueName, true, consumer);

        while (true) {
        }

    }
}
