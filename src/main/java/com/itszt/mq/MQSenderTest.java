package com.itszt.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消息发送
 */
public class MQSenderTest {

    private final static String QUEUE_NAME = "haha";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建一个链接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.0.144");
        //获取一个TCP链接
        Connection connection = connectionFactory.newConnection();
        //创建通道channal用于传输message
        Channel channel = connection.createChannel();
        //为了发送消息我们必须声明一个要发送消息的目标队列,然后我们往队列中发送消息
        //声明一个队列是等幂的,如果队列不存在会创建一个，消息内容是一个字节数据，所以你可以随心所欲的编辑你的内容
        //开始声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //消息
        String message = "我爱你haha";
        //消息发布
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        channel.close();
        connection.close();
    }
}
