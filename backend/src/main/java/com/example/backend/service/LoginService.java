package com.example.backend.service;
import com.example.backend.entity.User;
import com.example.backend.pojo.ResponseResult;
import com.example.backend.result.Result;

public interface LoginService {
    Result<User> login(User user);

    ResponseResult logout();
}
