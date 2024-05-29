package com.example.backend.controller;

import com.example.backend.entity.User;
import com.example.backend.result.Result;
import com.example.backend.service.SignupService;
import com.example.backend.service.UserService;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController
{
//    session的字段名
    public static final String SESSION_NAME = "userInfo";

    @Autowired
    private SignupService signupService;

    /**
     * 用户注册
     *
     * //@param user    传入注册用户
     * //@param errors  Validation的校验错误存放对象
     * //@param request 请求对象，用于操作session
     * //@return 注册结果
     */
    @PostMapping("/user/register")
    public Result<User> register(@RequestBody @Valid User user, BindingResult errors, HttpServletRequest request) {
        Result<User> result = new Result<>();
        //首先检查校验
        if(errors.hasErrors()){
            return result.error("Validation校验失败，http的body不为JSON格式的数据！");
        }
        //调用注册服务
        result = signupService.signup(user);
        return result;
    }

    //

}

