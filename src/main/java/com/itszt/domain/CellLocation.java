package com.itszt.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CellLocation {

    //    CellRangeAddress region = new CellRangeAddress(0, // first row
//            0, // last row
//            0, // first column
//            2 // last column
//    );
//sheet.addMergedRegion(region);
    private Integer firstRow;
    private Integer lastRow;
    private Integer firstColumn;
    private Integer lastColumn;
    private String cellName;
    private String cellScore;
    private String cellPath;
    private String cellValue;
}
