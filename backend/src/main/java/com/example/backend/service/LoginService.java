package com.example.backend.service;
import com.example.backend.entity.User;
import com.example.backend.pojo.ResponseResult;
public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
