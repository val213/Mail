package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User>
{
    //根据id返回用户信息
    User findUserById(Integer id);
    //根据name返回用户信息
    User findUserByName(String username);
    //根据邮箱返回用户信息
    User findUserByEmail(String emailAddress);
    //注册用户
    int registerUser(User user);
    //修改用户信息
    int editorUser(User user);
}
