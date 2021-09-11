package com.itszt.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "members")
public class Members {

    private String id;
    private String groupId;
    private String groupName;
    private String memberName;
    private String memberId;

}
