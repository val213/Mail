package com.example.backend.service;
import com.example.backend.entity.User;
import com.example.backend.pojo.ResponseResult;
import com.example.backend.result.Result;
import org.springframework.stereotype.Service;

@Service
public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
