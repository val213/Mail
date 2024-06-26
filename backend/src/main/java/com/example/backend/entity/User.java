package com.example.backend.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("user")
public class User implements Serializable
{
	@TableId(value="id", type=IdType.AUTO)
	private Integer id;
	private String username;
	private String emailAddress;
	// @NotEmpty的作用是在验证的时候，不允许为空
	@NotEmpty(message="密码不能为空！")
	private String password;
	private String telephone;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
	// 用户登录
	@TableField(exist=false)
	private List<String> permissions=new ArrayList<>();
	;
	@TableField(exist=false)
	@JSONField(serialize=false)
	private List<SimpleGrantedAuthority> authorities;
	
	
	public User(Integer id,String username,String emailAddress,String password,LocalDateTime createTime,
			LocalDateTime updateTime)
	{
		this.id=id;
		this.username=username;
		this.emailAddress=emailAddress;
		this.password=password;
		this.createTime=createTime;
		this.updateTime=updateTime;
	}
	
	
	public Collection<? extends GrantedAuthority> getAuthorities()
	{
		if(authorities!=null)
		{
			return authorities;
		}
		// 把permissions中String类型的权限信息封装成
		authorities=permissions.stream()
		                       .map(SimpleGrantedAuthority::new)
		                       .collect(Collectors.toList());
		return authorities;
	}
}
