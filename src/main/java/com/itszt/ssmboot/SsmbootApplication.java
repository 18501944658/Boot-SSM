package com.itszt.ssmboot;


import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.Arrays;

@SpringBootApplication(scanBasePackages = "com.itszt", exclude = {DataSourceAutoConfiguration.class})
@MapperScan(basePackages = "com.itszt.repositry")
@EnableTransactionManagement
@EnableRabbit
public class SsmbootApplication {

    private static final String[] BANNER = {"||=============================||",
                                            "||++      SSmbootApplication ++||",
                                            "||  ++         Server      ++  ||",
                                            "||    ++        启       ++    ||",
                                            "||      +++++   动   +++++     ||",
                                            "||    ++        成       ++    ||",
                                            "||  ++          功         ++  ||",
                                            "||++           over          ++||",
                                            "||=============================||"};

    public static void main(String[] args) {
        SpringApplication.run(SsmbootApplication.class, args);
        Arrays.asList(BANNER).forEach(System.out::println);
    }

}
