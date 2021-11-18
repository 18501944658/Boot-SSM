package com.itszt.controller;

import com.itszt.domain.UserDemo;
import com.itszt.util.TestApplicationContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Map;

/**
 * http模拟浏览器文件上传
 */
@Slf4j
@RestController
@RequestMapping("/upload")
public class HttpFileUpload {






    @GetMapping("/get")
    public UserDemo saveUser() {
        return TestApplicationContext.getDemo();
    }

}
