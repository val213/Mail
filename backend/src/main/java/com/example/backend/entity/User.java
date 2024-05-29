package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
	private String username;
	private String emailAddress;
	private String phoneNumber;
	private String password;
	private LocalDateTime createTime;
	private LocalDateTime updateTime;
}
