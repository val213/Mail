package com.example.backend.service.impl;


import com.example.backend.mapper.UserMapper;
import com.example.backend.result.Result;
import com.example.backend.utils.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.example.backend.entity.User;
import com.example.backend.pojo.ResponseResult;
import com.example.backend.service.LoginService;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private UserMapper userMapper;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public ResponseResult logout() {
        // 从SecurityContextHolder中清除认证信息
        SecurityContextHolder.clearContext();
        // 移除token认证
        return new ResponseResult(200, "登出成功");
    }

    @Override
    public ResponseResult<User> login(User user) {
//        // 用户认证
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(user.getEmailAddress(), user.getPassword());
//        System.out.println(authenticationToken.toString());
//        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
//        System.out.println("yanzhengguol");
        boolean authenticateResult = false;
        if(Objects.equals(user.getPassword(), userMapper.findUserByEmail(user.getEmailAddress()).getPassword())){
            authenticateResult = true;
        }
        //System.out.println(user.getPassword());
        //System.out.println(userMapper.findUserByEmail(user.getEmailAddress()).getPassword());
        // 认证通过
        if (authenticateResult) {
            // 认证通过，使用userid生成jwt，jwt存入ResponseResult返回
            String jwt = JwtUtil.createJWT(user.getId().toString(), null);
            Map<String, String> map = new HashMap<>();
            map.put("token", jwt);
            System.out.println("find user!");
            return new ResponseResult(200, "登录成功", jwt);
        } else {
            // 认证没通过，给出提示
            return new ResponseResult(303, "登录失败");
        }
    }
}