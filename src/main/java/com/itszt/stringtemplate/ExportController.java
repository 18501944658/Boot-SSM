package com.itszt.stringtemplate;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@RestController
@RequestMapping(value = "/export/")
public class ExportController {


    @RequestMapping(value = "/xml/multi")
    public void  exportMultiExcel(HttpServletResponse response, HttpServletRequest request) throws IOException {
        long start= System.currentTimeMillis();
        getStr(response);
        System.out.println("totalTime:"+(System.currentTimeMillis()-start));
    }


//    private static void buildExcelDocument(String fileName, Workbook wb, HttpServletRequest request, HttpServletResponse response) {
//        try {
//            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
//            response.setCharacterEncoding("UTF-8");
//            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "utf-8"));
//            response.flushBuffer();
//            wb.write(response.getOutputStream());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void getStr(HttpServletResponse out) {
        StringBuffer sb = new StringBuffer();
        try {
            out.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            out.setCharacterEncoding("UTF-8");
            out.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("白名单.xlsx", "utf-8"));
            out.flushBuffer();
//            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(new File("C:\\Users\\Administrator\\Desktop\\aa.xls")),"UTF-8");
//            BufferedWriter output = new BufferedWriter(write);
            ServletOutputStream output= out.getOutputStream();

            sb.append("<?xml version=\"1.0\"?>");
            sb.append("\n");
            sb.append("<?mso-application progid=\"Excel.Sheet\"?>");
            sb.append("\n");
            sb.append("<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\"");
            sb.append("\n");
            sb.append("  xmlns:o=\"urn:schemas-microsoft-com:office:office\"");
            sb.append("\n");
            sb.append(" xmlns:x=\"urn:schemas-microsoft-com:office:excel\"");
            sb.append("\n");
            sb.append(" xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\"");
            sb.append("\n");
            sb.append(" xmlns:html=\"http://www.w3.org/TR/REC-html40\">");
            sb.append("\n");
            sb.append(" <Styles>\n");
            sb.append("  <Style ss:ID=\"Default\" ss:Name=\"Normal\">\n");
            sb.append("   <Alignment ss:Vertical=\"Center\"/>\n");
            sb.append("   <Borders/>\n");
            sb.append("   <Font ss:FontName=\"宋体\" x:CharSet=\"134\" ss:Size=\"12\"/>\n");
            sb.append("   <Interior/>\n");
            sb.append("   <NumberFormat/>\n");
            sb.append("   <Protection/>\n");
            sb.append("  </Style>\n");
            sb.append(" </Styles>\n");
            //行数
            int rowNum = 10000;

            int currentRecord = 0;
            //总数据量
            int total = 300000;
            //列数
            int columnNum = 10;
            //第一个工作表
            sb.append("<Worksheet ss:Name=\"Sheet0\">");
            sb.append("\n");
            sb.append("<Table ss:ExpandedColumnCount=\"" + columnNum
                    + "\" ss:ExpandedRowCount=\"" + rowNum
                    + "\" x:FullColumns=\"1\" x:FullRows=\"1\">");
            sb.append("\n");
            for (int i = 0; i < total; i++) {
                if ((currentRecord == rowNum
                        || currentRecord > rowNum || currentRecord == 0)
                        && i != 0) {// 一个sheet写满
                    currentRecord = 0;
                    output.write(sb.toString().getBytes());
                    sb.setLength(0);
                    sb.append("</Table>");
                    sb.append("<WorksheetOptions xmlns=\"urn:schemas-microsoft-com:office:excel\">");
                    sb.append("\n");
                    sb.append("<ProtectObjects>False</ProtectObjects>");
                    sb.append("\n");
                    sb.append("<ProtectScenarios>False</ProtectScenarios>");
                    sb.append("\n");
                    sb.append("</WorksheetOptions>");
                    sb.append("\n");
                    sb.append("</Worksheet>");

                    sb.append("<Worksheet ss:Name=\"Sheet" + i / rowNum
                            + "\">");
                    sb.append("\n");
                    sb.append("<Table ss:ExpandedColumnCount=\"" + columnNum
                            + "\" ss:ExpandedRowCount=\"" + rowNum
                            + "\" x:FullColumns=\"1\" x:FullRows=\"1\">");
                    sb.append("\n");
                }
                sb.append("<Row>");
                for (int j = 0; j < columnNum; j++) {
                    sb.append("<Cell><Data ss:Type=\"String\">第"+(i+1)+"列第"+(j+1)+"行</Data></Cell>");
                    sb.append("\n");
                }
                sb.append("</Row>");
                //每三百行数据批量提交一次
                if (i % 300 == 0) {
                    System.out.println("提交了");
                    output.write(sb.toString().getBytes());
                    output.flush();
                    sb.setLength(0);
                }
                sb.append("\n");
                currentRecord++;
            }
            output.write(sb.toString().getBytes());
            sb.setLength(0);
            sb.append("</Table>");
            sb.append("<WorksheetOptions xmlns=\"urn:schemas-microsoft-com:office:excel\">");
            sb.append("\n");
            sb.append("<ProtectObjects>False</ProtectObjects>");
            sb.append("\n");
            sb.append("<ProtectScenarios>False</ProtectScenarios>");
            sb.append("\n");
            sb.append("</WorksheetOptions>");
            sb.append("\n");
            sb.append("</Worksheet>");
            sb.append("</Workbook>");
            sb.append("\n");
            output.write(sb.toString().getBytes());
            output.flush();
            output.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
