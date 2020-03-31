package com.itszt.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Id;
import java.time.LocalDateTime;


@Data
@Setter
@Getter
@ToString
public class User {

    @Id
    private Integer id;

    private String name;

    private Integer age;

    private LocalDateTime time;
}
