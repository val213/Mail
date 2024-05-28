package com.example.backend.service.impl;

import com.example.backend.entity.User;
import com.example.backend.mapper.UserMapper;
import com.example.backend.result.Result;
import com.example.backend.service.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignupServiceimpl implements SignupService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<User> signup(User user){
        Result<User> Result = new Result<>();

        //先查找注册用户是否存在
        User getUser = userMapper.findUserByName(user.getUsername());
        if(getUser != null) {
            Result.error("用户已存在！");
        }
        //TODO:加密用户密码
        userMapper.registerUser(user);
        return Result.success();
    }
}
