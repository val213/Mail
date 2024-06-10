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
	private Integer type;//1我发送的 2我收到的 3星标 4已读 5未读
	private String theme;
	private Integer pageNumber;
	private Integer pageSize;
}
