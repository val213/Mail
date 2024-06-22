package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailSendDTO implements Serializable
{
	private Integer senderId;
	private String targetEmailAddress;
	private String theme;
	private String content;
	private Integer draft;
	private Integer star;
	private Integer readis;
	private Integer junk;
}
