package com.example.backend.service;

import com.aliyuncs.exceptions.ClientException;
import com.example.backend.pojo.ResponseResult;

public interface VerificationService {
    ResponseResult sendVerificationCode(String telephone) throws ClientException;
    Boolean verifyCode(String phone, String code);


}
