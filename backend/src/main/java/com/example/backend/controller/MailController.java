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
		if(!originalFilename.isEmpty())
		{
			String extendedname=originalFilename.substring(originalFilename.lastIndexOf("."));
			String url=aliOssUtil.upload(mailSendDTO.getMultipartFile()
			                                        .getBytes(),UUID.randomUUID() + extendedname,originalFilename);
			mail.setAttachmentName(originalFilename);
			mail.setUrl(url);
		}
		mail.setSendTime(LocalDateTime.now());
		mail.setSize(mailSendDTO.getMultipartFile()
		                        .getSize());
		User targetUser=userService.getOne(new QueryWrapper<>(User.class).eq("email_address",
				mailSendDTO.getTargetEmailAddress()));
		mail.setReceiverId(targetUser.getId());
		mail.setOwnerId(mail.getSenderId());
		mailService.save(mail);
		mail.setOwnerId(mail.getReceiverId());
		mail.setId(null);
		mailService.save(mail);
		return Result.success();
	}
	
	
	@Operation(summary="分页查询邮件")
	@RequestMapping(value="/mail/view", method=RequestMethod.POST)
	@CrossOrigin
	public Result<PageResult> view(@RequestBody MailViewDTO mailViewDTO)
	{
		QueryWrapper<Mail> mailQueryWrapper=
				new QueryWrapper<Mail>().eq(mailViewDTO.getType()!=null && mailViewDTO.getType()==1,"sender_id",
						                        mailViewDTO.getUserId())
		                                                            .eq(mailViewDTO.getType()!=null && mailViewDTO.getType()==2,"receiver_id",mailViewDTO.getUserId())
		                                                            .eq(mailViewDTO.getStar()!=null && mailViewDTO.getStar()==1,"star",1)
		                                                            .eq(mailViewDTO.getStar()!=null && mailViewDTO.getStar()==0,"star",0)
		                                                            .eq(mailViewDTO.getRead()!=null && mailViewDTO.getRead()==0,"read",0)
		                                                            .eq(mailViewDTO.getRead()!=null && mailViewDTO.getRead()==1,"read",1)
		                                                            .like(mailViewDTO.getTheme()!=null,"theme",
				                                                            mailViewDTO.getTheme())
		                                                            .eq("owner_id",mailViewDTO.getUserId());
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
			                             .star(mail.getStar())
			                             .read(mail.getReadis())
			                             .build());
		}
		return Result.success(new PageResult(mailPage.getTotal(),mailViewVOList));
	}
	
	
	//>>>>>>> 66e5ebf581b15cdaef92415cb44be26cee8d2e9d
	@Operation(summary="批量删除邮件")
	@RequestMapping(value="/mail/delete", method=RequestMethod.DELETE)
	@CrossOrigin
	public Result<?> deleteBatch(@RequestParam List<Integer> ids)
	{
		mailService.removeBatchByIds(ids);
		return Result.success();
	}
	
	
	@Operation(summary="批量星标邮件")
	@RequestMapping(value="/mail/star", method=RequestMethod.PUT)
	@CrossOrigin
	public Result<?> starBatch(@RequestParam List<Integer> ids)
	{
		Mail mail;
		for(int id: ids)
		{
			mail=mailService.getById(id);
			mail.setStar(1);
			mailService.updateById(mail);
		}
		return Result.success();
	}
	
	
	@Operation(summary="批量取消星标邮件")
	@RequestMapping(value="/mail/cancelstar", method=RequestMethod.PUT)
	@CrossOrigin
	public Result<?> cancelstarBatch(@RequestParam List<Integer> ids)
	{
		Mail mail;
		for(int id: ids)
		{
			mail=mailService.getById(id);
			mail.setStar(0);
			mailService.updateById(mail);
		}
		return Result.success();
	}
}
