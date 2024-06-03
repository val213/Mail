package com.example.backend.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.example.backend.pojo.ResponseResult;
import com.example.backend.properties.AlismsProperties;
import com.example.backend.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VerificationServiceImpl implements VerificationService {
    // 使用ConcurrentHashMap来存储验证码和手机号的映射关系  也可以用session存储
    private static ConcurrentHashMap<String, String> verificationCodeMap = new ConcurrentHashMap<>();
    @Autowired
    private AlismsProperties alismsProperties;
    // 生成随机验证码
    private static String generateVerificationCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }
    // 存储验证码
    public void storeVerificationCode(String telephone, String code) {
        verificationCodeMap.put(telephone, code);
    }

    @Override
    public ResponseResult sendVerificationCode(String telephone) throws ClientException {
        // 生成验证码
        String code = generateVerificationCode();
        // 打印控制台日志
        System.out.println("验证码：" + code);
        System.out.println("telephone: " + telephone);
        // 存储验证码
        storeVerificationCode(telephone, code);
        // 发送验证码
        sendSms(telephone, code);
        // 返回成功信息
        return new ResponseResult(200, "已将验证码发送到您的手机号:" + telephone);
    }

    @Override
    public Boolean verifyCode(String telephone, String inputCode) {
        System.out.println("verifycode use , telephone: "+telephone +"inputCode: " + inputCode);
        // 从hashmap中获取存储的验证码
        String storedCode = verificationCodeMap.get(telephone);
        System.out.println("storedCode: " + storedCode);
        // 验证输入的验证码是否正确
        return storedCode != null && storedCode.equals(inputCode);
    }

    // 发送短信的方法
    private void sendSms(String phone, String code) throws ClientException {
        // 设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        // 初始化ascClient需要的几个参数
        final String product = "Dysmsapi"; // 短信API产品名称（短信产品名固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com"; // 短信API产品域名（接口地址固定，无需修改）
        // 替换成你的AK
        final String accessKeyId = alismsProperties.getAccessKeyId();// 你的accessKeyId
        final String accessKeySecret = alismsProperties.getAccessKeySecret(); // 你的accessKeySecret
        // 初始化ascClient
        IClientProfile profile = DefaultProfile.getProfile("CN-Hangzhou", accessKeyId, accessKeySecret);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        // 组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        // 使用post提交
        request.setMethod(com.aliyuncs.http.MethodType.POST);
        // 必填:待发送手机号
        request.setPhoneNumbers(phone);
        // 必填:短信签名-可在短信控制台中找到
        request.setSignName(alismsProperties.getSignName());
        // 必填:短信模板-可在短信控制台中找到
        request.setTemplateCode(alismsProperties.getTemplateCode());
        // 可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam("{\"name\":\"Tom\", \"code\":\"" + code + "\"}");
        // hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if (sendSmsResponse.getCode() != null) {
            if (sendSmsResponse.getCode().equals("OK")) {
                // 请求成功
                System.out.println("短信发送成功");
            } else {
                // 请求失败
                System.out.println("短信发送失败，错误码：" + sendSmsResponse.getCode() + "，错误信息：" + sendSmsResponse.getMessage());
            }
        }
    }

}

