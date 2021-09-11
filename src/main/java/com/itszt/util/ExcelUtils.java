package com.itszt.util;

import com.itszt.domain.CellLocation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: caokai
 */
@Slf4j
public class ExcelUtils {
    private final static String EXCEL2003 = "xls";
    private final static String EXCEL2007 = "xlsx";


    public static <T> void writeExcel(HttpServletRequest request, HttpServletResponse response, List<T> dataList, Class<T> cls, String fileName, List<CellLocation> mergeDate) {
        Field[] fields = cls.getDeclaredFields();
        List<Field> fieldList = Arrays.stream(fields).filter(field -> {
            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
            if (annotation != null && annotation.col() > 0) {
                field.setAccessible(true);
                return true;
            }
            return false;
        }).sorted(Comparator.comparing(field -> {
            int col = 0;
            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
            if (annotation != null) {
                col = annotation.col();
            }
            return col;
        })).collect(Collectors.toList());

        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet1");
        /***记录每列宽度**/
        Map<Integer, Integer> columnWidth = new HashMap<>();
        AtomicInteger ai = new AtomicInteger();
        {
            Row row = sheet.createRow(ai.getAndIncrement());
            AtomicInteger aj = new AtomicInteger();
            // 写入头部
            fieldList.forEach(field -> {
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                String columnName = "";
                if (annotation != null) {
                    columnName = annotation.value();
                }
                int index = aj.getAndIncrement();
                Cell cell = row.createCell(index);
                CellStyle cellStyle = wb.createCellStyle();
//				cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
                cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                /**设置自动换行**/
                cellStyle.setWrapText(true);
                Font font = wb.createFont();
                font.setBold(true);
                cellStyle.setFont(font);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(columnName);
                int width = columnName.getBytes().length * 2 * 180;
                columnWidth.put(index, width);

            });
        }
        if (!dataList.isEmpty()) {
            dataList.forEach(t -> {
                Row row1 = sheet.createRow(ai.getAndIncrement());
                AtomicInteger aj = new AtomicInteger();
                fieldList.forEach(field -> {
                    Class<?> type = field.getType();
                    Object value = "";
                    try {
                        value = field.get(t);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    int index = aj.getAndIncrement();
                    Cell cell = row1.createCell(index);
                    if (value != null) {
                        if (type == Date.class) {
                            cell.setCellValue(value.toString());
                        } else {
                            cell.setCellValue(value.toString());
                        }
                        cell.setCellValue(value.toString());
                        Integer headWidth = columnWidth.get(index);
                        int width = value.toString().getBytes().length * 2 * 180;

                        if (width < headWidth) {
                            width = headWidth;
                        }
                        if (width > 65280) {
                            width = 65279;
                        }
                        sheet.setColumnWidth(index, width);
                    }
                });
            });
        }


//                CellRangeAddress region = new CellRangeAddress(0,0,
//                        0,5);
//        sheet.addMergedRegion(region);
//        CellRangeAddress region = new CellRangeAddress(0, // first row
//                0, // last row
//                0, // first column
//                2 // last column
//        );
//        sheet.addMergedRegion(region);

        if (!mergeDate.isEmpty()) {
            mergeDate.stream().forEach(r -> {
                CellRangeAddress region = new CellRangeAddress(r.getFirstRow(),
                        r.getLastRow(),
                        r.getFirstColumn(),
                        r.getLastColumn());
                sheet.addMergedRegion(region);
            });
        }
        // 冻结窗格
        wb.getSheet("Sheet1").createFreezePane(0, 1, 0, 1);
        // 浏览器下载excel
        buildExcelDocument(fileName + ".xlsx", wb, request, response);
        // 生成excel文件
        // buildExcelFile(".\\default.xlsx",wb);
    }

    /**
     * 浏览器下载excel
     *
     * @param fileName
     * @param wb
     * @param response
     */

    private static void buildExcelDocument(String fileName, Workbook wb, HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
            response.flushBuffer();
            wb.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param response
     * @param cls
     * @param exportData
     * @param fileName
     * @throws IOException
     */
    public static void exportFile(HttpServletResponse response,
                                  Class<?> cls, List exportData, String fileName) {
        /**获取需要导出csv字段名称**/
        Field[] fields = cls.getDeclaredFields();
        List<Field> fieldList = Arrays.stream(fields).filter(field -> {
            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
            if (annotation != null && annotation.col() > 0) {
                field.setAccessible(true);
                return true;
            }
            return false;
        }).sorted(Comparator.comparing(field -> {
            int col = 0;
            ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
            if (annotation != null) {
                col = annotation.col();
            }
            return col;
        })).collect(Collectors.toList());

        try {
            // 写入临时文件
            File tempFile = File.createTempFile("vehicle", ".csv");
            BufferedWriter csvFileOutputStream = null;
            // UTF-8使正确读取分隔符","
            csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile), "GBK"), 1024);
            //先写入头部字段
            String headstr = fieldList.parallelStream().flatMap(field -> {
                ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
                String columnName = "";
                if (annotation != null) {
                    columnName = annotation.value();
                }
                return Stream.of(columnName);
            }).collect(Collectors.joining(","));
            csvFileOutputStream.write(headstr);
            csvFileOutputStream.newLine();
            //写入文件内容
            if (!exportData.isEmpty()) {
                for (Object t : exportData) {
                    for (Field field : fieldList) {
                        Object value = "";
                        try {
                            value = field.get(t);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (value != null) {
                            csvFileOutputStream.write(value.toString());
                        } else {
                            csvFileOutputStream.write("-");
                        }
                        /**最后一个字段不加,**/
                        if (!fieldList.get(fieldList.size() - 1).getName().equals(field.getName()))
                            csvFileOutputStream.write(",");
                    }
                    csvFileOutputStream.newLine();
                }
            }
            csvFileOutputStream.flush();

            /**
             * 写入csv结束，写出流
             */
            OutputStream out = response.getOutputStream();
            byte[] b = new byte[10240];
            File fileLoad = new File(tempFile.getCanonicalPath());
            response.reset();
            response.setContentType("application/csv");
            response.setHeader("Content-Disposition", "attachment;  filename=" + new String(fileName.getBytes("GBK"), "ISO8859-1"));
            long fileLength = fileLoad.length();
            String length1 = String.valueOf(fileLength);
            response.setHeader("Content_Length", length1);
            FileInputStream in = new FileInputStream(fileLoad);
            int n;
            while ((n = in.read(b)) != -1) {
                out.write(b, 0, n); // 每次写入out1024字节
            }
            in.close();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}

