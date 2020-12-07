package com.itszt.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class EmitLog {


    private static final String EXCHANGE_NAME = "logs";


    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.0.144");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        //我们声明一个Exchage的名称和类型,之后我们将produer发布的消息发送到声明exchange中
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        String message = "haha我的媳妇";
        //我们开始从producer发布消息到exchange交换器中
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());

        System.out.println("Sender message" + message);

        channel.close();
        connection.close();

    }
}
