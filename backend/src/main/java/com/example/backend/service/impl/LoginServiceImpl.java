package com.example.backend.service.impl;


import com.example.backend.mapper.UserMapper;
import com.example.backend.result.Result;
import com.example.backend.utils.JwtUtil;
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

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public ResponseResult logout() {
        // 从SecurityContextHolder中清除认证信息
        SecurityContextHolder.clearContext();
        return new ResponseResult(200, "登出成功");
    }

    @Override
    public ResponseResult<User> login(User user) {
        // 用户认证
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getEmailAddress(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // 认证通过
        if (authenticate.isAuthenticated()) {
            // 认证通过，使用userid生成jwt，jwt存入ResponseResult返回
            User authenticatedUser = (User) authenticate.getPrincipal();
            String jwt = JwtUtil.createJWT(authenticatedUser.getId().toString(), null);
            Map<String, String> map = new HashMap<>();
            map.put("token", jwt);
            return new ResponseResult(200, "登录成功", map);
        } else {
            // 认证没通过，给出提示
            return new ResponseResult(303, "登录失败");
        }
    }
}