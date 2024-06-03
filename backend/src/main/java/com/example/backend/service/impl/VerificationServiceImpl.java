package com.example.backend.service.impl;

import com.example.backend.service.VerificationService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class VerificationServiceImpl implements VerificationService {
    //存储手机号和验证码的哈希表
    private final Map<String, String> verificationCodes = new HashMap<>();

    @Override
    public String sendVerificationCode(String phoneNumber) {
        String code = generateVerificationCode();  //生成随机验证码
        // 这里添加发送短信的逻辑
        // sendSms(phoneNumber, code);
        verificationCodes.put(phoneNumber, code);
        return code;
    }

    @Override
    public boolean verifyCode(String phoneNumber, String code) {
        String storedCode = verificationCodes.get(phoneNumber);
        return storedCode != null && storedCode.equals(code);
    }

    private String generateVerificationCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));  //六位数验证码,如果生成的长度不足六位则在前面补0
    }
}

