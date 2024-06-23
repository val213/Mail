package com.example.backend.config;

import com.example.backend.filter.TokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    @Autowired
    private TokenFilter tokenFilter;

    public WebConfiguration(TokenFilter tokenFilter){
        this.tokenFilter=tokenFilter;
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry){

        //排除对注册登录的请求拦截
        List<String> excludePath = new ArrayList<>();
        excludePath.add("/user/login");
        excludePath.add("/user/register");
        excludePath.add("/user/sendverifycode");
        excludePath.add("/mail/maildetails/{mailId}");

        registry.addInterceptor(tokenFilter)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePath);
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
