package com.itszt.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Table;
import java.text.NumberFormat;
import java.util.Date;

/**
 * 企业风险第二张表，可以查到事件源企业以及pathId作为查询传导路径的关键参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subpath")
public class SubPath {
    //信号表对应id
    private String signalId;
    //事件id
    private String eventId;
    //企业id(最终影响的企业的id)
    private String entid;
    //事件时间
    private Date eventTime;
    //路径id
    private String pathId;
    //路径贡献值
    private String pathValue;
    //事件源企业id
    private String srcId;
    //事件源企业名称
    private String srcName;
    //传导步数
    private String step;
    //当前路径贡献度
    private Double percent;
    //当前路径贡献度百分数
    private String percentSrc;
    //预警时间
    private Date warnTime;

    //信号来源
    private String source;
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    public Date getWarnTime() {
        return warnTime;
    }

    public void setWarnTime(Date warnTime) {
        this.warnTime = warnTime;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    public Date getEventTime() {
        return eventTime;
    }

    public void setEventTime(Date eventTime) {
        this.eventTime = eventTime;
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPercentSrc() {
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(2);// 小数点后保留几位
        String str = nf.format(percent);
        return str;
    }

    public Double getPathValueDouble() {
        if(StringUtils.isEmpty(this.pathValue)){
            return 0d;
        }
        return Double.parseDouble(this.pathValue);
    }

    public void setPercentSrc(String percentSrc) {
        this.percentSrc = percentSrc;
    }
}
