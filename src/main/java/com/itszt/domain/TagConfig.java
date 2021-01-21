package com.itszt.domain;

import lombok.*;

import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "credit_tagconfig")
public class TagConfig implements Serializable {

    private String id;
    private String tagkey;
    private String tagname;
    private String inmodeltime;
    private Integer catagory1;
    private Integer catagory2;
    private Integer tagtype;
    private Integer sort;
    private Integer showtype;
    private LocalDate createdate;
    private String threshold;
    private String symbol;

}
