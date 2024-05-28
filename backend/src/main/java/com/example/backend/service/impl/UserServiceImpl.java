package com.example.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.User;
import com.example.backend.mapper.UserMapper;
import com.example.backend.result.Result;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService
{
    @Autowired
    private UserMapper userMapper;
    public Result<User> update(User user){
        Result<User> result = new Result<>();
        User getuser = userMapper.findUserByName(user.getUsername());
        if(getuser == null) {
            return result.error("不存在该用户！");
        }
        //修改用户
        userMapper.editorUser(user);
        return result.success();
    }
}
