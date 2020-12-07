package com.itszt.domain;

import lombok.*;

import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "orderjob")
public class OrderJob implements Serializable {

    private String id;

    private LocalDateTime startDate;

    private LocalDateTime createTime;
}
