package com.example.backend.service.impl;

import com.example.backend.entity.User;
import com.example.backend.mapper.UserMapper;
import com.example.backend.result.Result;
import com.example.backend.service.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.example.backend.service.VerificationService;

import java.time.LocalDateTime;

@Service
public class SignupServiceimpl implements SignupService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private VerificationService verificationService;

    @Override
    public Result<User> signup(User user, String code) {
        Result<User> result = new Result<>();

        //现在这一段会报错，是因为之前已经往数据库里用同一个手机号注册了多个账号，后面统一一个手机号对应一个账号即可
/*        //1.再查找注册的手机号是否已经存在
        User getUser1 = userMapper.findUserByTelephone(user.getTelephone());
        if(getUser1 != null){
            return Result.error("该手机号已被注册！请重新选择手机号！");
        }*/

        //2.先查找注册的用户名是否存在
        //之所以要限定一个账号对应一个用户名，是因为后面的邮箱分配由用户名决定，
        // 而邮箱又是唯一的，因此必须限定用户名的唯一性
        User getUser2 = userMapper.findUserByName(user.getUsername());
        if (getUser2 != null) {
            return Result.error("该用户名已被注册，请重新设置用户名！");
        }


        // 用户不存在，就开始创建新用户，首先需要比对验证码信息的正确性
        if (!verificationService.verifyCode(user.getTelephone(), code)) {
            return Result.error("验证码错误！");

        }

        //符合注册新用户的条件，通过mapper对数据库进行操作

        //1.分配id

//         2.加密用户密码
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(user.getPassword());
        System.out.println(user.getPassword().length());

        //3. 设置创建时间和更新时间
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        //4. 分配邮箱
        user.setEmailAddress(user.getUsername()+"@yunxin.com");

        // 5.注册用户
        userMapper.registerUser(user);

        return Result.success(user);
    }
}


