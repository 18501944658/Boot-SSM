//package com.itszt.mailController;
//
//import com.knowlegene.core.spring.ApplicationContextAccessor;
//import com.knowlegene.credit.grade.base.dto.GradeInfoDto;
//import org.apache.commons.collections.MapUtils;
//
//import javax.activation.DataHandler;
//import javax.activation.DataSource;
//import javax.activation.FileDataSource;
//import javax.mail.*;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeBodyPart;
//import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimeMultipart;
//import java.io.File;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//
///**
// * 使用SMTP协议发送电子邮件
// */
//public class SendEmailUtilsMultiModel {
//
//    /***初始化连接邮件服务器的会话信息***/
//    private static Properties props = null;
//
//    private static Transport transport = null;
//    /**
//     * 获取email发送服务端配置信息
//     ***/
//    private static EmailConfig emailConfig = ApplicationContextAccessor.getBean(EmailConfig.class);
//
//    static {
//        props = new Properties();
//        props.setProperty("mail.transport.protocol", emailConfig.getProtocol());
//        props.setProperty("mail.smtp.host", emailConfig.getHost());
//        props.setProperty("mail.smtp.port", emailConfig.getPort());
//        props.setProperty("mail.smtp.auth", emailConfig.getIsAuth());
//        props.setProperty("mail.debug", emailConfig.getIsEnabledDebugMod());
//        Session session = Session.getInstance(props, new MyAuthenticator(emailConfig.getAddress(), emailConfig.getPassword()));
//        try {
//            // 获得Transport实例对象
//            transport = session.getTransport();
//            transport.connect();
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 发送邮件
//     * param  username 用户名
//     * num  总数量
//     * status 变化状态
//     * changeNum 上日变化数量
//     * resultList 表格结果集
//     * <p>
//     * param.put("username", "门伟进");
//     * param.put("num", "12");
//     * param.put("status", "新增/减少/无变化");
//     * param.put("changeNum", "123");
//     * param.put("mail", "gaojianyu@knowlegene.com");
//     *
//     * @param param
//     */
//    public static void sendMail(Map<String, String> param, List<GradeInfoDto> resultList) {
////        param.put("username", "门伟进");
////        param.put("num", "12");
////        param.put("status", "新增/减少/无变化");
////        param.put("changeNum", "123");
////        param.put("mail", "gaojianyu@knowlegene.com");
//        String username = MapUtils.getString(param, "username", "");
//        String num = MapUtils.getString(param, "num", "");
//        String status = MapUtils.getString(param, "status", "新增/减少/无变化");
//        String changeNum = MapUtils.getString(param, "changeNum", "");
//        String mail = MapUtils.getString(param, "mail", "");
//        String contentHtmlStr = getContentHtmlStr(username, num, status, changeNum, resultList);
//        try {
//            sendHtmlEmail(mail, contentHtmlStr);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * 发送简单的html邮件
//     */
//    public static boolean sendHtmlEmail(String to, String content) throws Exception {
//        try {
//            /** 创建Session实例对象**/
//            Session session = Session.getInstance(props, new MyAuthenticator(emailConfig.getAddress(), emailConfig.getPassword()));
//
//            /**  创建MimeMessage实例对象**/
//            MimeMessage message = new MimeMessage(session);
//            /**  设置邮件主题**/
//            message.setSubject("北京知因智慧");
//            /**  设置发送人**/
//            message.setFrom(new InternetAddress(emailConfig.getAddress()));
//            /**  设置发送时间**/
//            message.setSentDate(new Date());
//            /**  设置收件人**/
//            message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(to));
//            /**  设置html内容为邮件正文，指定MIME类型为text/html类型，并指定字符编码为gbk**/
////            message.setContent(content, "text/html;charset=utf-8");
//            /**  创建一个MIME子类型为"mixed"的MimeMultipart对象，表示这是一封混合组合类型的邮件**/
//            MimeMultipart mailContent = new MimeMultipart("mixed");
//            message.setContent(mailContent);
//            MimeBodyPart mailBody = new MimeBodyPart();
//            mailContent.addBodyPart(mailBody);
//            /** 邮件正文(内嵌图片+html文本)**/
//            MimeMultipart body = new MimeMultipart("related");  //邮件正文也是一个组合体,需要指明组合关系
//            mailBody.setContent(body);
//
//            /** 邮件正文由html和图片构成**/
//            MimeBodyPart imgPart = new MimeBodyPart();
//            MimeBodyPart htmlPart = new MimeBodyPart();
//            body.addBodyPart(imgPart);
//            body.addBodyPart(htmlPart);
//            /**知因logo图片**/
//            File file = new File("credit-grade\\grade-base\\src\\main\\resources\\pic\\logo.png");
//            /** 正文图片**/
//            DataSource ds3 = new FileDataSource(file.getAbsolutePath());
//            DataHandler dh3 = new DataHandler(ds3);
//            imgPart.setDataHandler(dh3);
//            imgPart.setContentID("logo.png");
//
//            /** html邮件内容**/
//            MimeMultipart htmlMultipart = new MimeMultipart("alternative");
//            htmlPart.setContent(htmlMultipart);
//            MimeBodyPart htmlContent = new MimeBodyPart();
//            htmlContent.setContent(
//                    content +
//                            "<br><br><br><span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src='cid:logo.png' /></span>"
//                    , "text/html;charset=gbk");
//            htmlMultipart.addBodyPart(htmlContent);
//            /** 保存并生成最终的邮件内容**/
//            message.saveChanges();
//            /**  将message对象传递给transport对象，将邮件发送出去**/
//            transport.sendMessage(message, message.getAllRecipients());
//            System.err.println("发送邮件成功");
//            /**  关闭连接**/
//            transport.close();
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//
//    }
//
//
//    /**
//     * 向邮件服务器提交认证信息
//     */
//    static class MyAuthenticator extends Authenticator {
//
//        private String username;
//
//        private String password;
//
//        public MyAuthenticator() {
//            super();
//        }
//
//        public MyAuthenticator(String username, String password) {
//            super();
//            this.username = username;
//            this.password = password;
//        }
//
//
//        @Override
//        protected PasswordAuthentication getPasswordAuthentication() {
//
//            return new PasswordAuthentication(username, password);
//        }
//    }
//
//    public static StringBuilder
//    getTableStart(String head[], String title) {
//
//        StringBuilder table = new StringBuilder();
//        table.append("    <html>");
//        table.append("     <head>");
//        table.append("      <title> New Document </title>");
//        table.append("     </head>");
//        table.append("    ");
//        table.append("    <style type=\"text/css\">");
//        table.append("    table { ");
//        table.append("      margin: 10px 0 30px 0;");
//        table.append("      font-size: 13px;");
//        table.append("    }");
//        table.append("    ");
//        table.append("    table caption { ");
//        table.append("      text-align:left;");
//        table.append("    }");
//        table.append("    ");
//        table.append("    table tr th { ");
//        table.append("      background: #3B3B3B;");
//        table.append("      color: #FFF;");
//        table.append("      padding: 7px 4px;");
//        table.append("      text-align: left;");
//        table.append("    }");
//        table.append("    ");
//        table.append("    table tr td { ");
//        table.append("      color: #FFF;");
//        table.append("      padding: 7px 4px;");
//        table.append("      text-align: left;");
//        table.append("    }");
//        table.append("    ");
//        table.append("    table tr.odd{");
//        table.append("        background-color:#cef;");
//        table.append("    }");
//        table.append("    ");
//        table.append("    table tr.even{");
//        table.append("        background-color:#ffc;");
//        table.append("    }");
//        table.append("      ");
//        table.append("    table tr td { ");
//        table.append("      color: #47433F;");
//        table.append("      border-top: 1px solid #FFF;");
//        table.append("    }");
//        table.append("     </style>");
//        table.append("    ");
//        table.append("     <body>");
//        table.append("<h2>" + title + "<h2/>");
//        table.append("    <table style=\"width:500px; border-spacing:0;\">  ");
//        table.append("       <tr>  ");
//        for (int i = 0; i < head.length; i++) {
//            table.append("          <th>" + head[i] + "</th>  ");
//        }
//        table.append("       </tr>  ");
//        return table;
//    }
//
//    public static StringBuilder getTableEnd(StringBuilder table) {
//        table.append("    </table> ");
//        table.append("     </body>");
//        table.append("     <font size=3 color=red>以上为系统基于您的信息定制自动推送,请勿回复!</font>");
//        table.append("    </html>");
//        return table;
//    }
//
//    /**
//     * DEDE
//     *
//     * @param username   用户名
//     * @param num        表格数据量
//     * @param status     变更状态
//     * @param changeNum  变化数量
//     * @param resultList 表格数据
//     * @param <
//     * @return
//     */
//    public static String getContentHtmlStr(String username, String num, String status, String changeNum, List<GradeInfoDto> resultList) {
//        /**写死表格头***/
//        String head[] = {"序号", "企业名称", "注册资本(万元)", "企业性质", "上市板块", "行业", "区域", "预警时间", "风险等级变化", "是否债卷违约", "最新外部评级"};
//        /***欢迎词**/
//        StringBuilder tableStart = getTableStart(head, "<font size=3><b>尊敬的“" + username + "”用户:</b></font><br>" +
//                "&nbsp;&nbsp;&nbsp;&nbsp;<font size=3><b>您好!知因智慧对您已关注的企业风险进行持续监控,今日预警企业共 家,较上日“" + status + "”" + changeNum + "家</b></font><br>" +
//                "<font size=3><b>具体信息如下:</b></font><br>");
//        if (resultList == null || resultList.isEmpty()) {
//            StringBuilder table = getTableEnd(tableStart);
//            return table.toString();
//        }
//        for (int i = 0; i < resultList.size(); i++) {
//            String tr = "<tr class=\"odd\">";
//            if (i % 2 == 1) {
//                tr = "<tr class=\"even\">";
//            }
//            GradeInfoDto gradeInfoDto = resultList.get(i);
//            tableStart.append("     " + tr + "    ");
//            tableStart.append("         <td>" + i + 1 + "</td>  ");
//            tableStart.append("         <td>" + gradeInfoDto.getEntName() + "</td>  ");
//            tableStart.append("         <td>" + gradeInfoDto.getRegcap() + "</td>  ");
//            tableStart.append("         <td>" + gradeInfoDto.getEntNatureName() + "</td>  ");
//            tableStart.append("         <td>" + gradeInfoDto.getListedType() + "</td>  ");
//            tableStart.append("         <td>" + gradeInfoDto.getEntIndustryName() + "</td>  ");
//            tableStart.append("         <td>" + gradeInfoDto.getEntAreaName() + "</td>  ");
//            tableStart.append("         <td>" + gradeInfoDto.getBatchTime() + "</td>  ");
//            tableStart.append("         <td>" + gradeInfoDto.getRiskLevelChange() + "</td>  ");
//            tableStart.append("         <td>" + gradeInfoDto.getBreakFlg() + "</td>  ");
//            tableStart.append("         <td>" + gradeInfoDto.getExternalRatingChange() + "</td>  ");
//            tableStart.append("       </tr>  ");
//        }
//        StringBuilder table = getTableEnd(tableStart);
//        return table.toString();
//    }
//}
