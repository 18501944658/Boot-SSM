package com.itszt.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.stereotype.Component;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;


@ApiModel
@Setter
@Getter
@Component
public class UserDemo implements Serializable, BeanNameAware {


    public UserDemo() {
    }

    private static final long serialVersionUID = 2579075550395835604L;
    @Id
    @ApiModelProperty(value = "用户id")
    private Integer id;
    @ApiModelProperty(value = "用户姓名")
    private String name;
    @ApiModelProperty(value = "用户年龄")
    private Integer age;
    @ApiModelProperty(value = "当前时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime time;


    @Override
    public void setBeanName(String s) {
        this.name=s;
    }

    @Override
    public String toString() {
        return "UserDemo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", time=" + time +
                '}';
    }
}
