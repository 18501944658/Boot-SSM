package com.itszt.common;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@Data
@ToString
public class BaseParmerters {

    private Integer pageNum;

    private Integer pageSize;


    @NotEmpty
    private String uid;
}
