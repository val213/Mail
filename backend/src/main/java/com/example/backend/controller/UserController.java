package com.example.backend.controller;

import com.aliyuncs.exceptions.ClientException;
import com.example.backend.entity.User;
import com.example.backend.pojo.ResponseResult;
import com.example.backend.result.Result;
import com.example.backend.service.SignupService;
import com.example.backend.service.UserService;
import com.example.backend.service.VerificationService;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "*")
public class UserController
{
//    session的字段名
    public static final String SESSION_NAME = "userInfo";

    @Autowired
    private SignupService signupService;

    @Autowired
    private VerificationService verificationService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * 用户注册
     *
     * //@param user    传入注册用户
     * //@param errors  Validation的校验错误存放对象
     * //@param request 请求对象，用于操作session
     * //@return 注册结果
     */
    @PostMapping("/user/register")
    public Result<User> register(@Valid @RequestBody User user, @RequestParam String verifycode, BindingResult errors, HttpServletRequest request) {
        Result result = new Result<>();

        // 打印请求参数
        logger.info("用户名: " + user.getUsername());
        logger.info("密码: " + user.getPassword());
        logger.info("完整的 User 对象: " + user.toString());

        // 检查校验结果
        if (errors.hasErrors()) {
            return Result.error("Validation校验失败，http的body不为JSON格式的数据！");
        }

        // 调用注册服务
        result = signupService.signup(user,verifycode);
        return result;
    }

    @PostMapping("/user/sendverifycode")
    public ResponseResult sendVerifyCode(@RequestParam String telephone) throws ClientException {
        System.out.println("telephone in controller: " + telephone);
        // 调用发送验证码服务
        return  verificationService.sendVerificationCode(telephone);
    }
}

