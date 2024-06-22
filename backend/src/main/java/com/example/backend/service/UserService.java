package com.example.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.backend.entity.User;
import com.example.backend.result.Result;

public interface UserService extends IService<User>
{
    //修改用户信息，更新用户信息
    Result<User> update(User user);

    //使用用户邮箱获取用户信息
    User getUserByEmail(String email);
}
