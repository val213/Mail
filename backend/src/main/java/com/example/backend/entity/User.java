package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("user")
public class User implements Serializable
{
	@TableId(value="id",type=IdType.AUTO)
	private Integer id;
	@NotEmpty(message = "用户名不能为空！")
	private String username;
	@NotEmpty(message = "邮箱不能为空！")
	private String emailAddress;
	@NotEmpty(message = "密码不能为空！")
	private String password;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
}
