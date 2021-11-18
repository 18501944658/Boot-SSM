package com.itszt.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class News implements Serializable {

    /**企业entid**/
    private String entid;
    /***新闻标题**/
    private String title;
    /**新闻链接**/
    private String url;
    /**新闻时间**/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date newsDate;
    /**新闻内容**/
    private String content;
    /**企业有关新闻总条数**/
    private String totalCount;
}
