package com.example.backend.filter;

import com.example.backend.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TokenFilter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception{
        if(request.getMethod().equals("OPTIONS")){
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        response.setCharacterEncoding("utf-8");
        String token = request.getHeader("Authorization");
        if(token != null){
            boolean result = isVaildToken(token);
            if(result){
                System.out.println("通过token验证");
                return result;
            }
        }
        try{
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "token verity fail");
            System.out.println("未通过token验证");
        } catch(Exception e){
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return false;
        }
        return false;
    }


    private boolean isVaildToken(String jwt){
        try{
            JwtUtil.parseJWT(jwt);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
