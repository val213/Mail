package com.example.backend.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailViewVO implements Serializable
{
	private Integer id;
	private String senderUsername;
	private String receiverUsername;
	private String theme;
	private String sendTime;
	private Integer star;
	private Integer read;
	private String Summary;
	private Integer draft;
	private Integer junk;
}
