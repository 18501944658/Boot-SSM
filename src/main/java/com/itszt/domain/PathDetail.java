package com.itszt.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import java.util.Date;

//风险传导路径
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pathdetail")
public class PathDetail  {

    private String pathId;

    private String entId;

    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private Date eventTime;

    private String signalId;

    private String toId;

    private String fromId;

    private Double score;

    private String relationtype;

    private String fromName;

    private String toName;



}
