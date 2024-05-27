package com.example.backend.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.backend.entity.User;
import com.example.backend.pojo.ResponseResult;
import com.example.backend.service.LoginService;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {

    /**
     * 登出
     * @return
     */
    @Override
    public ResponseResult logout() {

        return null;
    }

    /**
     * 登录
     * @return
     */
    @Override
    public ResponseResult login(User user) {
        System.out.println("login use");
//        VerificationService verificationService = new VerificationServiceImpl();
//        // 调用Verification的验证函数，如果验证码错误，给出提示
//        if (!verificationService.verifyCode(user.getTelephone(), user.getVerificationCode())) {
//            System.out.println("Verification code error");
//            return new ResponseResult(303, "验证码错误");
//        }else{
//            System.out.println("Verification code correct");
//        }
        // 用户认证

        System.out.println("Login success");
        return new ResponseResult(200, "登录成功");
    }
}
