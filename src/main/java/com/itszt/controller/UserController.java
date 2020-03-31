package com.itszt.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itszt.common.BaseParmerters;
import com.itszt.common.RestResponse;
import com.itszt.domain.User;
import com.itszt.repositry.UserDao;
import com.itszt.service.UserService;
import com.itszt.util.VerifyParamsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class UserController {


    @Autowired
    private UserService userService;

    @PostMapping
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

}
