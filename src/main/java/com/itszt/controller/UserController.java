package com.itszt.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itszt.common.BaseParmerters;
import com.itszt.common.RestResponse;
import com.itszt.config.ApplicationContextAccessor;
import com.itszt.domain.User;
import com.itszt.domain.UserDemo;
import com.itszt.service.UserService;
import com.itszt.util.VerifyParamsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;


@RestController
@Api(value = "用户操作表", tags = "user表")
public class UserController {



    @Autowired
    private UserService userService;


    @Autowired
    private RabbitTemplate rabbitTemplate;


    @PostMapping
    @ApiOperation(value = "保存用户信息save", tags = "保存")
    @RequestMapping("/save")
    public String saveUser(@RequestBody User user) {

        user.setTime(LocalDateTime.now());

        System.out.println("user = " + user);
        rabbitTemplate.convertAndSend("ExchangeDirect", "directing", user);
        return "ok";
    }


    @GetMapping
    @ApiOperation(value = "保存用户信息saves", tags = "保存s")
    @RequestMapping("/saves")
    public String saves() {

        for (int i = 0; i < 1000; i++) {
            User user = new User();
            user.setName("heiheiehei" + i);
            user.setAge(i);
            user.setTime(LocalDateTime.now());
            System.out.println("user = " + user);
            rabbitTemplate.convertAndSend("ExchangeDirect", "directing", user);
        }

        return "ok";
    }


    @GetMapping
    @ApiOperation(value = "保存用户信息saveall", tags = "保存all")
    @RequestMapping("/saveall")
    public String saveList() throws InterruptedException {
        long start = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(1000);
        for (int i = 0; i < 1000; i++) {
            new MyThread(countDownLatch, i).start();
            countDownLatch.countDown();
        }
        countDownLatch.await();
        System.out.println("间接入库总耗时:" + (System.currentTimeMillis() - start));
        return "ok";
    }


    @GetMapping
    @ApiOperation(value = "保存用户信息saveallno", tags = "保存allno")
    @RequestMapping("/saveallno")
    public String saveLists() throws InterruptedException {
        long start = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(1000);
        for (int i = 0; i < 1000; i++) {

            new MyRunThread(countDownLatch, i).start();
            countDownLatch.countDown();
        }
        countDownLatch.await();
        System.out.println("直接入库总耗时:" + (System.currentTimeMillis() - start));
        return "ok";
    }


    @PostMapping
    @ApiOperation(value = "查询用户信息select", tags = "查询")
    @RequestMapping("/select")
    RestResponse<List<User>> select(@RequestBody @Validated BaseParmerters baseParmerters, BindingResult bindingResult) {

        RestResponse restResponse = VerifyParamsUtil.verifyParams(bindingResult);
        if (restResponse != null) {
            return restResponse;
        }

        PageHelper.startPage(baseParmerters.getPageNum(), baseParmerters.getPageSize());
        List<User> users = userService.selecctAll(baseParmerters);
        PageInfo pageInfo = new PageInfo(users);

        return new RestResponse<List<User>>(pageInfo.getList());
    }


    @RequestMapping("/one")
    User selectOne() {
        return userService.selectByPrimaryKey(1);
    }

    @RequestMapping(value = "/all", method = RequestMethod.POST)
    List<User> getAll(@RequestBody @Validated BaseParmerters baseParmerters, BindingResult bindingResult) {
        List<User> users = userService.selecctAll(baseParmerters);
        return users;
    }


    @RequestMapping(value = "/addone", method = RequestMethod.GET)
    void add() {
        for (int i = 1000; i < 2000; i++) {
            User build = User.builder().age(i).id(i).name("dahuang" + i).time(LocalDateTime.now()).build();
            userService.insertOne(build);
        }
    }


    class MyThread extends Thread {
        CountDownLatch countDownLatch;
        int i;

        public MyThread(CountDownLatch countDownLatch, int i) {
            this.countDownLatch = countDownLatch;
            this.i = i;
        }

        @Override
        public void run() {

            for (int j = 0; j < 10; j++) {
                User user = new User();
                user.setName("ljjaixgt" + j);
                user.setAge(j);
                user.setTime(LocalDateTime.now());
//                System.out.println("countDownLatch: " + countDownLatch + "  i:  " + i + "当前入库的用户是 = " + user);
                rabbitTemplate.convertAndSend("ExchangeDirect", "directing", user);
            }


        }
    }


    class MyRunThread extends Thread {
        CountDownLatch countDownLatch;
        int i;

        public MyRunThread(CountDownLatch countDownLatch, int i) {
            this.countDownLatch = countDownLatch;
            this.i = i;
        }

        @Override
        public void run() {
            User user = new User();
            user.setName("haha" + i);
            user.setAge(i);
            user.setTime(LocalDateTime.now());
            System.out.println("countDownLatch: " + countDownLatch + "  i:  " + i + "当前入库的用户是 = " + user);
            userService.insertOne(user);
        }
    }

}
