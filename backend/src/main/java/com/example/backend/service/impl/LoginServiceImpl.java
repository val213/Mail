package com.example.backend.service.impl;


import com.example.backend.mapper.UserMapper;
import com.example.backend.result.Result;
import com.example.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    private AuthenticationManager authenticationManager;
    /**
     * 登出
     * @return
     */
    @Override
    public ResponseResult logout() {

        return null;
    }

    /**
     * 登录
     * @return
     */
    @Override
    public ResponseResult<User> login(User user) {
        System.out.println("login use");
        // 用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getEmailAddress(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        System.out.println("authenticate: " + authenticate);
        // 认证没通过，给出提示
        if (Objects.isNull(authenticate)) {
            System.out.println("Login fail");
            return new ResponseResult(303, "登录失败");
        }
//        // 验证码认证
//        VerificationService verificationService = new VerificationServiceImpl();
//        // 调用Verification的验证函数，如果验证码错误，给出提示
//        if (!verificationService.verifyCode(user.getTelephone(), user.getVerificationCode())) {
//            System.out.println("Verification code error");
//            return new ResponseResult(303, "验证码错误");
//        }else{
//            System.out.println("Verification code correct");
//        }

        // 认证通过，使用userid生成jwt，jwt存入ResponseResult返回
        User User = (User) authenticate.getPrincipal();
        String userId = User.getUsername();
        String jwt = JwtUtil.createJWT(userId, null);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        System.out.println("jwt: " + jwt);

        User getuser = userMapper.findUserByName(user.getUsername());
        // 检查邮箱地址是否存在
        if(getuser == null) {
            return new ResponseResult(303, "不存在该用户！");
        }
        //检查该用户的密码是否正确
        if(user.getPassword() != getuser.getPassword()){
            return new ResponseResult(303, "密码错误！");
        }
        System.out.println("Login success");
        return new ResponseResult(200, "登录成功", map);
    }
}
