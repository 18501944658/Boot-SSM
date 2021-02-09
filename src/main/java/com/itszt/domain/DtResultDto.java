package com.itszt.domain;

import com.itszt.util.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DtResultDto {

    private String toId          ;
    /**查询企业名称*/
    @ExcelColumn(value = "客户名称",col = 1)
    private String searchName;
    /**事件源企业**/
    private String srcName       ;
    private String srcGroupId;
    private String srcGroupName;
    private String relationtype  ;
    @ExcelColumn(value = "原始风险分",col = 2)
    private String srcIdScore    ;
    @ExcelColumn(value = "受影响企业",col = 3)
    private String toName        ;
    private String toGroupId;
    private String toGroupName;
    private String srcId         ;
    private String fromIdScore   ;
    private String fromName      ;
    private String fromGroupId;
    private String fromGroupName;
    private String pathId        ;
    @ExcelColumn(value = "受影响企业分值",col = 4)
    private String toIdScore     ;
    private String fromId        ;
    @ExcelColumn(value = "传导路径",col = 5)
    private String path;
    @ExcelColumn(value = "路径长度",col = 6)
    private String step;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DtResultDto that = (DtResultDto) o;
        return toId.equals(that.toId) &&
                srcName.equals(that.srcName) &&
                srcGroupId.equals(that.srcGroupId) &&
                srcGroupName.equals(that.srcGroupName) &&
                relationtype.equals(that.relationtype) &&
                srcIdScore.equals(that.srcIdScore) &&
                toName.equals(that.toName) &&
                toGroupId.equals(that.toGroupId) &&
                toGroupName.equals(that.toGroupName) &&
                srcId.equals(that.srcId) &&
                fromIdScore.equals(that.fromIdScore) &&
                fromName.equals(that.fromName) &&
                fromGroupId.equals(that.fromGroupId) &&
                fromGroupName.equals(that.fromGroupName) &&
                pathId.equals(that.pathId) &&
                toIdScore.equals(that.toIdScore) &&
                fromId.equals(that.fromId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(toId, srcName, srcGroupId, srcGroupName, relationtype, srcIdScore, toName, toGroupId, toGroupName, srcId, fromIdScore, fromName, fromGroupId, fromGroupName, pathId, toIdScore, fromId, path);
    }
}
