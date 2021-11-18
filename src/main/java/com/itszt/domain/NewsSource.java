package com.itszt.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewsSource implements Serializable {

    private String TITLE	;//新闻标题
    private String SOURCE	;//来源
    private String SOURCE_URL;	//原文链接
    private String AUTHOR	;//作者
    private String PUB_DATE	;//发布时间
    private String TAGS	;//标签
    private String EMOTION	;//情感
    private String RELATED_OBJ	;//关联对象
    private String CONTENT	;//新闻内容
    private String ENT_ID	;//企业id
    private String ENT_NAME	;//企业名称
    private String URL;	//链接
    private String SAVE_TIME;	//存入时间
}
