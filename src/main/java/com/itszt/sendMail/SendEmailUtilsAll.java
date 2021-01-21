//package com.itszt.mailController;
//
//import com.itszt.domain.User;
//
//import javax.activation.DataHandler;
//import javax.activation.DataSource;
//import javax.activation.FileDataSource;
//import javax.mail.*;
//import javax.mail.internet.*;
//import java.io.*;
//import java.time.LocalDateTime;
//import java.util.*;
//
///**
// * 使用SMTP协议发送电子邮件
// */
//public class SendEmailUtilsAll {
//
//    // 邮件发送协议
//    private final static String PROTOCOL = "smtp";
//
//    // SMTP邮件服务器
//    private final static String HOST = "smtp.knowlegene.com";
//
//    // SMTP邮件服务器默认端口 25/465
//    private final static String PORT = "25";
//
//    // 是否要求身份认证
//    private final static String IS_AUTH = "true";
//
//    // 是否启用调试模式（启用调试模式可打印客户端与服务器交互过程时一问一答的响应消息）
//    private final static String IS_ENABLED_DEBUG_MOD = "true";
//
//    //发件人邮箱授权码
//    private final static String AUTHORIZATION_CODE = "你的邮箱用于登录第三方邮箱系统的授权码不是密码";
//
//    // 发件人
//    private static String from = " ";
//
//    // 收件人
//    private static String to = "email";
//
//
//
//    // 初始化连接邮件服务器的会话信息
//    private static Properties props = null;
//
//    private static Transport transport = null;
//
//    static {
//        props = new Properties();
//        props.setProperty("mail.transport.protocol", PROTOCOL);
//        props.setProperty("mail.smtp.host", HOST);
//        props.setProperty("mail.smtp.port", PORT);
//        props.setProperty("mail.smtp.auth", IS_AUTH);
//        props.setProperty("mail.debug",IS_ENABLED_DEBUG_MOD);
//        Session session = Session.getInstance(props, new MyAuthenticator());
//
//        try {
//            // 获得Transport实例对象
//            transport = session.getTransport();
//            // 打开连接
////            transport.connect(from, AUTHORIZATION_CODE);
//            transport.connect();
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public static void main(String[] args) throws Exception {
//        String contentHtmlStr = getContentHtmlStr("高建宇", "12", "新增", "121", null);
//        sendHtmlEmail(to,contentHtmlStr);
//    }
//    /**
//     * 发送简单的文本邮件
//     */
//    public static boolean sendTextEmail(String to,int code) throws Exception {
//        try {
//            // 创建Session实例对象
////            Session session1 = Session.getDefaultInstance(props);
//            Session session1 = Session.getInstance(props, new MyAuthenticator());
//            // 创建MimeMessage实例对象
//            MimeMessage message = new MimeMessage(session1);
//            // 设置发件人
//            message.setFrom(new InternetAddress(from));
//            // 设置邮件主题
//            message.setSubject("内燃机注册验证码");
//            // 设置收件人
//            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
//            // 设置发送时间
//            message.setSentDate(new Date());
//            // 设置纯文本内容为邮件正文
//            message.setText("您的验证码是："+code+"!验证码有效期是10分钟，过期后请重新获取！"
//                    + "中国内燃机学会");
//            // 保存并生成最终的邮件内容
//            message.saveChanges();
//
//            // 将message对象传递给transport对象，将邮件发送出去
//            transport.sendMessage(message, message.getAllRecipients());
//            // 关闭连接
//            transport.close();
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//
//
//    /**
//     * 发送简单的html邮件
//     */
//    public static boolean sendHtmlEmail(String to, String content) throws Exception {
//        try {
//            // 创建Session实例对象
//            Session session = Session.getInstance(props, new MyAuthenticator());
//
//            // 创建MimeMessage实例对象
//            MimeMessage message = new MimeMessage(session);
//            // 设置邮件主题
//            message.setSubject("北京知因智慧");
//            // 设置发送人
//            message.setFrom(new InternetAddress(from));
//            // 设置发送时间
//            message.setSentDate(new Date());
//            // 设置收件人
//            message.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(to));
//            // 设置html内容为邮件正文，指定MIME类型为text/html类型，并指定字符编码为gbk
//            message.setContent(content,"text/html;charset=utf-8");
//
//            // 保存并生成最终的邮件内容
//            message.saveChanges();
//            // 将message对象传递给transport对象，将邮件发送出去
//            transport.sendMessage(message, message.getAllRecipients());
//            System.err.println("发送邮件成功");
//            // 关闭连接
//            transport.close();
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//
//    }
//
//    /**
//     * 发送带内嵌图片的HTML邮件
//     */
//    public static void sendHtmlWithInnerImageEmail() throws MessagingException {
//        // 创建Session实例对象
//        Session session = Session.getDefaultInstance(props, new MyAuthenticator());
//
//        // 创建邮件内容
//        MimeMessage message = new MimeMessage(session);
//        // 邮件主题,并指定编码格式
//        message.setSubject("带内嵌图片的HTML邮件", "utf-8");
//        // 发件人
//        message.setFrom(new InternetAddress(from));
//        // 收件人
//        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
//        // 抄送
//        message.setRecipient(Message.RecipientType.CC, new InternetAddress("java_test@sohu.com"));
//        // 密送 (不会在邮件收件人名单中显示出来)
//        message.setRecipient(Message.RecipientType.BCC, new InternetAddress("417067629@qq.com"));
//        // 发送时间
//        message.setSentDate(new Date());
//
//        // 创建一个MIME子类型为“related”的MimeMultipart对象
//        MimeMultipart mp = new MimeMultipart("related");
//        // 创建一个表示正文的MimeBodyPart对象，并将它加入到前面创建的MimeMultipart对象中
//        MimeBodyPart htmlPart = new MimeBodyPart();
//        mp.addBodyPart(htmlPart);
//        // 创建一个表示图片资源的MimeBodyPart对象，将将它加入到前面创建的MimeMultipart对象中
//        MimeBodyPart imagePart = new MimeBodyPart();
//        mp.addBodyPart(imagePart);
//
//        // 将MimeMultipart对象设置为整个邮件的内容
//        message.setContent(mp);
//
//        // 设置内嵌图片邮件体
//        DataSource ds = new FileDataSource(new File("resource/firefoxlogo.png"));
//        DataHandler dh = new DataHandler(ds);
//        imagePart.setDataHandler(dh);
//        imagePart.setContentID("firefoxlogo.png");  // 设置内容编号,用于其它邮件体引用
//
//        // 创建一个MIME子类型为"alternative"的MimeMultipart对象，并作为前面创建的htmlPart对象的邮件内容
//        MimeMultipart htmlMultipart = new MimeMultipart("alternative");
//        // 创建一个表示html正文的MimeBodyPart对象
//        MimeBodyPart htmlBodypart = new MimeBodyPart();
//        // 其中cid=androidlogo.gif是引用邮件内部的图片，即imagePart.setContentID("androidlogo.gif");方法所保存的图片
//        htmlBodypart.setContent("<span style='color:red;'>这是带内嵌图片的HTML邮件哦！！！<img src=\"cid:firefoxlogo.png\" /></span>","text/html;charset=utf-8");
//        htmlMultipart.addBodyPart(htmlBodypart);
//        htmlPart.setContent(htmlMultipart);
//
//        // 保存并生成最终的邮件内容
//        message.saveChanges();
//
//        // 发送邮件
//        // 将message对象传递给transport对象，将邮件发送出去
//        transport.sendMessage(message, message.getAllRecipients());
//        System.err.println("发送邮件成功");
//        // 关闭连接
//        transport.close();
//    }
//
//    /**
//     * 发送带内嵌图片、附件、多收件人(显示邮箱姓名)、邮件优先级、阅读回执的完整的HTML邮件
//     */
//    public static void sendMultipleEmail() throws Exception {
//        String charset = "utf-8";   // 指定中文编码格式
//        // 创建Session实例对象
//        Session session = Session.getInstance(props,new MyAuthenticator());
//
//        // 创建MimeMessage实例对象
//        MimeMessage message = new MimeMessage(session);
//        // 设置主题
//        message.setSubject("使用JavaMail发送混合组合类型的邮件测试");
//        // 设置发送人
//        message.setFrom(new InternetAddress(from,"新浪测试邮箱",charset));
//        // 设置收件人
//        message.setRecipients(MimeMessage.RecipientType.TO,
//                new Address[] {
//                        // 参数1：邮箱地址，参数2：姓名（在客户端收件只显示姓名，而不显示邮件地址），参数3：姓名中文字符串编码
//                        new InternetAddress("java_test@sohu.com", "张三_sohu", charset),
//                        new InternetAddress("xyang0917@163.com", "李四_163", charset),
//                }
//        );
//        // 设置抄送
//        message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress("xyang0917@gmail.com","王五_gmail",charset));
//        // 设置密送
//        message.setRecipient(Message.RecipientType.BCC, new InternetAddress("xyang0917@qq.com", "赵六_QQ", charset));
//        // 设置发送时间
//        message.setSentDate(new Date());
//        // 设置回复人(收件人回复此邮件时,默认收件人)
//        message.setReplyTo(InternetAddress.parse("\"" + MimeUtility.encodeText("田七") + "\" <417067629@qq.com>"));
//        // 设置优先级(1:紧急   3:普通    5:低)
//        message.setHeader("X-Priority", "1");
//        // 要求阅读回执(收件人阅读邮件时会提示回复发件人,表明邮件已收到,并已阅读)
//        message.setHeader("Disposition-Notification-To", from);
//
//        // 创建一个MIME子类型为"mixed"的MimeMultipart对象，表示这是一封混合组合类型的邮件
//        MimeMultipart mailContent = new MimeMultipart("mixed");
//        message.setContent(mailContent);
//
//        // 附件
//        MimeBodyPart attach1 = new MimeBodyPart();
//        MimeBodyPart attach2 = new MimeBodyPart();
//        // 内容
//        MimeBodyPart mailBody = new MimeBodyPart();
//
//        // 将附件和内容添加到邮件当中
//        mailContent.addBodyPart(attach1);
//        mailContent.addBodyPart(attach2);
//        mailContent.addBodyPart(mailBody);
//
//        // 附件1(利用jaf框架读取数据源生成邮件体)
//        DataSource ds1 = new FileDataSource("resource/Earth.bmp");
//        DataHandler dh1 = new DataHandler(ds1);
//        attach1.setFileName(MimeUtility.encodeText("Earth.bmp"));
//        attach1.setDataHandler(dh1);
//
//        // 附件2
//        DataSource ds2 = new FileDataSource("resource/如何学好C语言.txt");
//        DataHandler dh2 = new DataHandler(ds2);
//        attach2.setDataHandler(dh2);
//        attach2.setFileName(MimeUtility.encodeText("如何学好C语言.txt"));
//
//        // 邮件正文(内嵌图片+html文本)
//        MimeMultipart body = new MimeMultipart("related");  //邮件正文也是一个组合体,需要指明组合关系
//        mailBody.setContent(body);
//
//        // 邮件正文由html和图片构成
//        MimeBodyPart imgPart = new MimeBodyPart();
//        MimeBodyPart htmlPart = new MimeBodyPart();
//        body.addBodyPart(imgPart);
//        body.addBodyPart(htmlPart);
//
//        // 正文图片
//        DataSource ds3 = new FileDataSource("resource/firefoxlogo.png");
//        DataHandler dh3 = new DataHandler(ds3);
//        imgPart.setDataHandler(dh3);
//        imgPart.setContentID("firefoxlogo.png");
//
//        // html邮件内容
//        MimeMultipart htmlMultipart = new MimeMultipart("alternative");
//        htmlPart.setContent(htmlMultipart);
//        MimeBodyPart htmlContent = new MimeBodyPart();
//        htmlContent.setContent(
//                "<span style='color:red'>这是我自己用java mail发送的邮件哦！" +
//                        "<img src='cid:firefoxlogo.png' /></span>"
//                , "text/html;charset=gbk");
//        htmlMultipart.addBodyPart(htmlContent);
//
//        // 保存邮件内容修改
//        message.saveChanges();
//
//        /*File eml = buildEmlFile(message);
//        sendMailForEml(eml);*/
//
//        // 发送邮件
//        Transport.send(message);
//    }
//
//    /**
//     * 将邮件内容生成html文件
//     * @param message 邮件内容
//     */
//    public static File buildEmlFile(Message message) throws MessagingException, FileNotFoundException, IOException {
//        File file = new File("c:\\" + MimeUtility.decodeText(message.getSubject())+".eml");
//        message.writeTo(new FileOutputStream(file));
//        return file;
//    }
//
//    /**
//     * 发送本地已经生成好的email文件
//     */
//    public static void sendMailForEml(File eml) throws Exception {
//        // 获得邮件会话
//        Session session = Session.getInstance(props,new MyAuthenticator());
//        // 获得邮件内容,即发生前生成的eml文件
//        InputStream is = new FileInputStream(eml);
//        MimeMessage message = new MimeMessage(session,is);
//        // 发送邮件
//        Transport.send(message);
//    }
//
//    /**
//     * 向邮件服务器提交认证信息
//     */
//    static class MyAuthenticator extends Authenticator {
//
//        private String username = "";
//
//        private String password = "";
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
//        @Override
//        protected PasswordAuthentication getPasswordAuthentication() {
//
//            return new PasswordAuthentication(username, password);
//        }
//    }
//
//    public static StringBuilder
//    getTableStart(String head[],String title){
//
//        StringBuilder table=new StringBuilder();
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
//        table.append("<h2>"+title+"<h2/>");
//        table.append("    <table style=\"width:500px; border-spacing:0;\">  ");
//        table.append("       <tr>  ");
//        for (int i=0;i<head.length;i++){
//            table.append("          <th>"+head[i]+"</th>  ");
//        }
//        table.append("       </tr>  ");
//        return table;
//    }
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
//     * @param username 用户名
//     * @param num      表格数据量
//     * @param status   变更状态
//     * @param changeNum  变化数量
//     * @param resultList 表格数据
//     * @param <T>
//     * @return
//     */
//    public  static<T>  String  getContentHtmlStr(String username,String num,String status,String changeNum,List<T> resultList){
//        String head[] = {"序号","企业名称","注册资本(万元),企业性质","上市板块","行业","区域","预警时间","风险等级变化","是否债卷违约","最新外部评级"};
//        List<User> users = new ArrayList<>(2);
//        User employee = new User();
//        employee.setName("小明");
//employee.setAge(121);
//        employee.setTime(LocalDateTime.now());
//        users.add(employee);
//        User employee1 = new User();
//        employee1.setName("小白");
//        employee1.setAge(121212);
//        employee1.setTime(LocalDateTime.now());
//        users.add(employee1);
//
//        StringBuilder tableStart = getTableStart(head,"<font size=3><b>尊敬的“"+username+"”用户:</b></font><br>"+
//                "&nbsp;&nbsp;&nbsp;&nbsp;<font size=3><b>您好!知因智慧对您已关注的企业风险进行持续监控,今日预警企业共 家,较上日“"+status+"”"+changeNum+"家</b></font><br>"+
//                 "<font size=3><b>具体信息如下:</b></font><br>");
//        for (int i=0;i<users.size();i++){
//            String tr = "<tr class=\"odd\">";
//            if (i%2==1){
//                tr = "<tr class=\"even\">";
//            }
//            tableStart.append("     "+tr+"    ");
//            tableStart.append("         <td>"+users.get(i).getAge()+"</td>  ");
//            tableStart.append("         <td>"+users.get(i).getAge()+"</td>  ");
//            tableStart.append("         <td>"+users.get(i).getAge()+"</td>  ");
//            tableStart.append("         <td>"+users.get(i).getAge()+"</td>  ");
//            tableStart.append("         <td>"+users.get(i).getAge()+"</td>  ");
//            tableStart.append("         <td>"+users.get(i).getName()+"</td>  ");
//            tableStart.append("         <td>"+users.get(i).getName()+"</td>  ");
//            tableStart.append("         <td>"+users.get(i).getTime()+"</td>  ");
//            tableStart.append("         <td>"+users.get(i).getTime()+"</td>  ");
//            tableStart.append("         <td>"+users.get(i).getTime()+"</td>  ");
//            tableStart.append("       </tr>  ");
//        }
//        StringBuilder table = getTableEnd(tableStart);
//        return table.toString();
//    }
//}
