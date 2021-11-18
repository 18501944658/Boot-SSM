package com.itszt.rocketmq;

public class JmsConfig {


    /**
     * Name Server 地址，因为是集群部署 所以有多个用 分号 隔开
     */
    public static final String NAME_SERVER = "192.168.100.101:9876";
    /**
     * 主题名称 主题一般是服务器设置好 而不能在代码里去新建topic（ 如果没有创建好，生产者往该主题发送消息 会报找不到topic错误）
     */
    /***
     * 请求获取指定企业的新闻详情
     */
    public static final String TOPIC_RESQUEST = "ent_news_request";
    /*获取json结果**/
    public static final String TOPIC_RESULT = "ent_news_result";
}
