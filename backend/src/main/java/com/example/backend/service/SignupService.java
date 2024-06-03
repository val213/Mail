package com.example.backend.service;

import com.example.backend.entity.User;
import com.example.backend.result.Result;

public interface SignupService {
    public Result signup(User user, String verificationCode);

}
