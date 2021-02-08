package com.itszt.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 企业传导预警类
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "insight")
public class Insight  {
    //企业id
    private String entid;
    //企业名称
    private String entname;
    //分值
    private Double totalValue;
    //预警时间
    private Date warnTime;
    //外部影响
    private Float outEventRisk;
    //自身影响
    private Float selfEventRisk;

    //等级变化 0不变 1低到中 2中到高 3低到高
    private Integer levelChange;
    //风险值变化
    private Double valueChange;
    //风险值变化
    private Double realValueChange;
    //变化之前的值
    private Double oldValue;
    //各路径分值相加求和分数
    private Float traceValueSum;
    //自身风险值
    private Float selfRisk;
    //节点类型(P的个人，C是企业)
    private String isCp;

    /**
     * 1 表示最新的传导结果
     * 0 表示非最新的传导结果
     * 3 表示你那边处理的记录
     */
    private Integer status;

    public Double getTotalValue() {
        BigDecimal b = new BigDecimal(totalValue);
        return b.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}



