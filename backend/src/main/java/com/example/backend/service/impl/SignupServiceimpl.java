package com.example.backend.service.impl;

import com.example.backend.entity.User;
import com.example.backend.mapper.UserMapper;
import com.example.backend.result.Result;
import com.example.backend.service.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.example.backend.service.VerificationService;
@Service
public class SignupServiceimpl implements SignupService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private VerificationService verificationService;

    @Override
    public Result<String> signup(User user){
        Result<String> result = new Result<>();

        //先查找注册用户是否存在
        User getUser = userMapper.findUserByName(user.getUsername());
        if(getUser != null) {
            return Result.error("用户已存在！");
        }

        // 发送验证码
        String verificationCode = verificationService.sendVerificationCode(user.getTelephone());

        return Result.success("验证码已发送！");
    }

    @Override
    public Result<User> completeSignup(User user) {
        Result<User> result = new Result<>();

        // 加密用户密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 注册用户
        userMapper.registerUser(user);

        return result.success(user);
    }
}
