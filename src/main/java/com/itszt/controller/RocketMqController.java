package com.itszt.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.itszt.domain.News;
import com.itszt.rocketmq.JmsConfig;
import com.itszt.rocketmq.Producer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
public class RocketMqController {

    @Autowired
    private Producer producer;

    /**
     * 发送请求消息获取结果
     *
     * @throws InterruptedException
     * @throws RemotingException
     * @throws MQClientException
     * @throws MQBrokerException
     */
    @RequestMapping(value = "/sendmsg", method = RequestMethod.GET)
    public void sendmsg() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {

        List<String> mesList = new ArrayList<>();
        mesList.add("小小");
        mesList.add("爸爸");
        mesList.add("妈妈");
        mesList.add("爷爷");
        mesList.add("奶奶");
        for (String s : mesList) {
            //创建生产信息
            Message message = new Message(JmsConfig.TOPIC_RESQUEST, "testtag", ("小小一家人的称谓:" + s).getBytes());
            //发送
            SendResult sendResult = producer.getProducer().send(message);
            producer.getProducer().send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("sendResult = " + sendResult);
                }

                @Override
                public void onException(Throwable throwable) {
                    System.out.println("throwable = " + throwable);
                }
            });
            log.info("输出生产者信息={}", sendResult);
        }
    }

    /**
     * 发送结果集
     *
     * @throws InterruptedException
     * @throws RemotingException
     * @throws MQClientException
     * @throws MQBrokerException
     */
    @RequestMapping(value = "/sendresult", method = RequestMethod.GET)
    public void sendresult() throws InterruptedException, RemotingException, MQClientException, MQBrokerException, IOException {

      List<News> newsList= new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            News news = new News();
            news.setContent("新闻内容");
            news.setTitle("新闻标题" + i);
            news.setUrl("新闻链接");
            news.setNewsDate(new Date());
            news.setEntid("企业entid");
            news.setTotalCount("新闻总条数");
            newsList.add(news);
        }
        String msgJson = JSON.toJSONString(newsList);
        System.out.println("msgJson = " + msgJson);
        //创建生产信息
            Message message = new Message(JmsConfig.TOPIC_RESULT, "resulttag", msgJson.getBytes("utf-8"));
            //发送
//            SendResult sendResult = producer.getProducer().send(message);
            producer.getProducer().send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    log.info("输出生产者信息={}", sendResult);
                }

                @Override
                public void onException(Throwable throwable) {
                    System.out.println("throwable = " + throwable);
                }
            });
//            log.info("输出生产者信息={}", sendResult);


    }

}
