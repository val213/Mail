package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("mail")
public class Mail
{
	@TableId(value="id", type=IdType.AUTO)
	private Integer id;
	private String theme;
	private String content;
	private String attachmentName;
	private Long size;
	private String url;
}
