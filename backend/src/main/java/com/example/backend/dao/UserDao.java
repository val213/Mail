package com.example.backend.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.example.backend.entity.User;
import com.example.backend.mapper.UserMapper;

@Repository
public class UserDao {
    @Autowired
    private UserMapper userMapper;

    //根据id返回用户信息
    public User findUserById(Integer id){return userMapper.findUserById(id);}
    //根据name返回用户信息
    public User findUserByName(String name){return userMapper.findUserByName(name);}
    //注册用户
    public int registerUser(User user){return userMapper.registerUser(user);}
    //修改用户信息
    public int editUser(User user){return userMapper.editorUser(user);}
}
