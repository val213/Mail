package com.example.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.dto.MailSendDTO;
import com.example.backend.dto.MailViewDTO;
import com.example.backend.entity.Mail;
import com.example.backend.entity.User;
import com.example.backend.result.PageResult;
import com.example.backend.result.Result;
import com.example.backend.service.MailService;
import com.example.backend.service.UserService;
import com.example.backend.utils.AliOssUtil;
import com.example.backend.vo.MailViewVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
		User targetUser=userService.getOne(new QueryWrapper<>(User.class).eq("email_address",
				mailSendDTO.getTargetEmailAddress()));
		mail.setReceiverId(targetUser.getId());
		mailService.save(mail);
		return Result.success();
	}
	
	
	@Operation(summary="分页查询邮件")
	@RequestMapping(value="/mail/view", method=RequestMethod.POST)
	@CrossOrigin
	public Result<PageResult> view(@RequestBody MailViewDTO mailViewDTO)
	{
		QueryWrapper<Mail> mailQueryWrapper=new QueryWrapper<Mail>().eq(mailViewDTO.getType()==1,"sender_id",
				                                                            mailViewDTO.getUserId())
		                                                            .eq(mailViewDTO.getType()==2,"receiver_id",
				                                                            mailViewDTO.getUserId());
		Page<Mail> mailPage=mailService.page(new Page<>(mailViewDTO.getPageNumber(),mailViewDTO.getPageSize()),
				mailQueryWrapper);
		List<Mail> mailList=mailPage.getRecords();
		List<MailViewVO> mailViewVOList=new ArrayList<>();
		for(Mail mail: mailList)
		{
			mailViewVOList.add(MailViewVO.builder()
			                             .id(mail.getId())
			                             .theme(mail.getTheme())
			                             .senderUsername(userService.getById(mail.getSenderId())
			                                                        .getUsername())
			                             .receiverUsername(userService.getById(mail.getReceiverId())
			                                                          .getUsername())
			                             .sendTime(mail.getSendTime()
			                                           .format(DateTimeFormatter.ofPattern("yyyy" + "/MM/dd " + "HH" + ":mm")))
			                             .build());
		}
		return Result.success(new PageResult(mailPage.getTotal(),mailViewVOList));
	}
}
