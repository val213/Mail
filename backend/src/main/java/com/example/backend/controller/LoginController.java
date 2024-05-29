package com.example.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.backend.entity.User;
import com.example.backend.pojo.ResponseResult;
import com.example.backend.service.LoginService;

public class LoginController {
    private LoginService loginService;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user)
    {
        System.out.println("user: " + user);
        return loginService.login(user);
    }

    @RequestMapping("/user/logout")
    public ResponseResult logout()
    {
        return loginService.logout();
    }
}
