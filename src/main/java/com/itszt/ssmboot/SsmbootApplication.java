package com.itszt.ssmboot;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(scanBasePackages = "com.itszt")
@MapperScan(basePackages = "com.itszt.repositry")
@EnableTransactionManagement
public class SsmbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsmbootApplication.class, args);
    }

}
