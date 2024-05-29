package com.example.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.backend.dto.MailSendDTO;
import com.example.backend.entity.Mail;
import com.example.backend.entity.User;
import com.example.backend.result.Result;
import com.example.backend.service.MailService;
import com.example.backend.service.UserService;
import com.example.backend.utils.AliOssUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Tag(name="邮件模块")
@RestController
public class MailController
{
	private final MailService mailService;
	private final AliOssUtil aliOssUtil;
	private final UserService userService;
	
	
	public MailController(MailService mailService,AliOssUtil aliOssUtil,UserService userService)
	{
		this.mailService=mailService;
		this.aliOssUtil=aliOssUtil;
		this.userService=userService;
	}
	
	
	@Operation(summary="发送邮件")
	@RequestMapping(value="/mail/send", method=RequestMethod.POST)
	@CrossOrigin
	public Result<?> sendMail(MailSendDTO mailSendDTO) throws IOException
	{
		Mail mail=new Mail();
		BeanUtils.copyProperties(mailSendDTO,mail);
		String originalFilename=mailSendDTO.getMultipartFile()
		                                   .getOriginalFilename();
		assert originalFilename!=null;
		String extendedname=originalFilename.substring(originalFilename.lastIndexOf("."));
		String url=aliOssUtil.upload(mailSendDTO.getMultipartFile()
		                                        .getBytes(),UUID.randomUUID() + extendedname);
		mail.setSendTime(LocalDateTime.now());
		mail.setSize(mailSendDTO.getMultipartFile()
		                        .getSize());
		mail.setAttachmentName(originalFilename);
		mail.setUrl(url);
		User targetUser=userService.getOne(new QueryWrapper<>(User.class).eq("email_address",mailSendDTO.getTargetEmailAddress()));
		mail.setReceiverId(targetUser.getId());
		mailService.save(mail);
		return Result.success();
	}
}
