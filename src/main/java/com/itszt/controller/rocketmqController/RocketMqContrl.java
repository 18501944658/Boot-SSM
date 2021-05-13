package com.itszt.controller.rocketmqController;

import com.itszt.rocketmq.JmsConfig;
import com.itszt.rocketmq.Producer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "mq")
public class RocketMqContrl {


    @Autowired
    private Producer producer;



    @RequestMapping(value = "/sendmsg",method = RequestMethod.GET)
    public void sendmessage() throws UnsupportedEncodingException {
        List<String> messages= new ArrayList<>();
        messages.add("小小");
        messages.add("爸爸");
        messages.add("妈妈");
        messages.add("爷爷");
        messages.add("奶奶");
//        messages.stream().forEach(r->{
            Message message = new Message(JmsConfig.TOPIC, "tags", "keys", "haha".getBytes(RemotingHelper.DEFAULT_CHARSET));
            DefaultMQProducer producer = this.producer.getProducer();
            try {
                SendResult sendResult = producer.send(message);
                log.info("输出生产者信息:{}",sendResult);
            } catch (MQClientException e) {
                e.printStackTrace();
            } catch (RemotingException e) {
                e.printStackTrace();
            } catch (MQBrokerException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//        });
    }
}
