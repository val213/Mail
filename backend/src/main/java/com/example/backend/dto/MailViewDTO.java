package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailViewDTO implements Serializable
{
	private Integer userId;
	private Integer type;// 1我发送的 2我收到的
	private Integer star;// 0非星标1星标
	private Integer readis;// 0未读1已读
	private String theme;// 按邮件主题模糊查询
	private Integer pageNumber;
	private Integer pageSize;
	private Integer draft;// 0非草稿1为草稿
	private Integer junk;// 0非丢弃1为丢弃
	private String ccEmailAddresses;// 抄送人邮箱地址
}
