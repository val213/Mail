package com.example.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.backend.entity.User;
import com.example.backend.pojo.ResponseResult;
import com.example.backend.service.LoginService;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;
    Logger logger = Logger.getLogger(LoginController.class.getName());
    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user)
    {
        logger.info("/user/login接口调用请求" + user);
        System.out.println("user: " + user);
        return loginService.login(user);
    }

    @RequestMapping("/user/logout")
    public ResponseResult logout()
    {
        return loginService.logout();
    }
}
