package com.itszt.sendMail;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmailConfig {

    /**
     * 邮件发送协议
     **/
    @Value("${ali.mail.protocol}")
    private String protocol;

    /**
     * SMTP邮件服务器
     **/
    @Value("${ali.mail.host}")
    private String host;


    /**
     * SMTP邮件服务器默认端口 25/465
     **/
    @Value("${ali.mail.port:25}")
    private String port;

    /**
     * 发件人服务器地址
     **/
    @Value("${ali.mail.address}")
    private  String address;
    /**
     * 邮箱地址
     **/
    @Value("${ali.mail.password}")
    private  String password;


    /**
     * 是否要求身份认证
     **/
    @Value("${ali.mail.isauth}")
    private  String isAuth;


    /**
     * 是否启用调试模式
     **/
    @Value("${ali.mail.isenableddebugmod}")
    private  String isEnabledDebugMod;


}
