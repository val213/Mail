package com.example.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.dto.MailDetailsDTO;
import com.example.backend.dto.MailSendDTO;
import com.example.backend.dto.MailViewDTO;
import com.example.backend.entity.Attachment;
import com.example.backend.entity.Mail;
import com.example.backend.entity.User;
import com.example.backend.result.PageResult;
import com.example.backend.result.Result;
import com.example.backend.service.MailService;
import com.example.backend.service.UserService;
import com.example.backend.service.AttachmentService;
import com.example.backend.utils.AliOssUtil;
import com.example.backend.vo.MailViewVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Optional;
@Slf4j
@Tag(name="邮件模块")
@RestController
public class MailController {
	private final MailService mailService;
	private final AliOssUtil aliOssUtil;
	private final UserService userService;

	private final AttachmentService attachmentService;
	public MailController(MailService mailService, AliOssUtil aliOssUtil, UserService userService, AttachmentService attachmentService) {
		this.mailService = mailService;
		this.aliOssUtil = aliOssUtil;
		this.userService = userService;
		this.attachmentService = attachmentService;
	}


	@Operation(summary = "发送邮件")
	@RequestMapping(value = "/mail/send", method = RequestMethod.POST)
	@CrossOrigin
	public Result<?> sendMail(@ModelAttribute MailSendDTO mailSendDTO,  @RequestParam("multipleFiles") Optional<List<MultipartFile>> multipleFiles) throws IOException
	{
		// 打印获取的请求参数
		log.info("发送邮件基础参数：" + mailSendDTO);
		log.info("附件有吗："+ multipleFiles.isPresent());
		Mail mail=new Mail();
		List<String> attachmentIds = new ArrayList<>();
		BeanUtils.copyProperties(mailSendDTO,mail);
		if (multipleFiles.isEmpty()){
			log.info("没有附件");
		}
		else
		{
			for (MultipartFile multipartFile : multipleFiles.get()) {
				String originalFilename = multipartFile.getOriginalFilename();
				assert originalFilename != null;
				if (!originalFilename.isEmpty()) {
					String extendedname = originalFilename.substring(originalFilename.lastIndexOf("."));
					String url = aliOssUtil.upload(multipartFile.getBytes(), UUID.randomUUID() + extendedname, originalFilename);

					Attachment attachment = new Attachment();
					attachment.setFileName(originalFilename);
					attachment.setDownloadUrl(url);
					attachment.setSize(multipartFile.getSize());
					attachment.setMimeType(multipartFile.getContentType());
					attachment.setCreatedAt(LocalDateTime.now());
					attachmentService.save(attachment); // 保存到数据库

					attachmentIds.add(String.valueOf(attachment.getId())); // 添加ID到列表
				}
			}
        }
		mail.setSendTime(LocalDateTime.now());
		mail.setAttachmentIds(String.join(",", attachmentIds)); // 设置attachmentIds字段
		// 打印收件人
		log.info("收件人邮箱地址：" + mailSendDTO.getTargetEmailAddress());
		User targetUser=userService.getOne(new QueryWrapper<>(User.class).eq("email_address",
				mailSendDTO.getTargetEmailAddress()));
		mail.setReceiverId(targetUser.getId());
		mail.setOwnerId(mail.getSenderId());
		mail.setStar(0);
		mail.setReadis(0);
		// 判断是否是草稿
		if (mailSendDTO.getDraft() == 1) {
			mail.setDraft(1);
		} else {
			mail.setDraft(0);
		}
		mailService.save(mail);
		mail.setOwnerId(mail.getReceiverId());
		mail.setId(null);
		mailService.save(mail);
		// 在 sendMail 方法中处理抄送的邮箱地址
		String ccEmailAddresses = mailSendDTO.getCcEmailAddresses();
		if (ccEmailAddresses != null && !ccEmailAddresses.isEmpty()) {
			String[] ccEmails = ccEmailAddresses.split(",");
			for (String ccEmail : ccEmails) {
				Mail ccMail = new Mail();
				BeanUtils.copyProperties(mail, ccMail); // 复制原始邮件的所有属性
				ccMail.setId(null); // 设置 id 为 null，以便创建一个新的邮件
				User ccUser = userService.getOne(new QueryWrapper<>(User.class).eq("email_address", ccEmail));
				if (ccUser != null) {
					ccMail.setReceiverId(ccUser.getId()); // 设置接收者 ID 为抄送用户的 ID
					ccMail.setOwnerId(ccUser.getId()); // 设置邮件的所有者 ID 为抄送用户的 ID
					mailService.save(ccMail); // 保存抄送的邮件
				}
			}
		}
		return Result.success();
	}


	@Operation(summary = "分页查询邮件")
	@RequestMapping(value = "/mail/view", method = RequestMethod.POST)
	@CrossOrigin
	public Result<PageResult> view(@RequestBody MailViewDTO mailViewDTO)
	{
		QueryWrapper<Mail> mailQueryWrapper=
				new QueryWrapper<Mail>().eq(mailViewDTO.getType()!=null && mailViewDTO.getType()==1,"sender_id",
						                        mailViewDTO.getUserId())
		                                                            .eq(mailViewDTO.getType()!=null && mailViewDTO.getType()==2,"receiver_id",mailViewDTO.getUserId())
		                                                            .eq(mailViewDTO.getStar()!=null && mailViewDTO.getStar()==1,"star",1)
		                                                            .eq(mailViewDTO.getStar()!=null && mailViewDTO.getStar()==0,"star",0)
		                                                            .eq(mailViewDTO.getReadis()!=null && mailViewDTO.getReadis()==0,"readis",0)
		                                                            .eq(mailViewDTO.getReadis()!=null && mailViewDTO.getReadis()==1,"readis",1)
																	.eq(mailViewDTO.getDraft()!=null && mailViewDTO.getDraft()==1,"draft",1)
																	.eq(mailViewDTO.getDraft()!=null && mailViewDTO.getDraft()==0,"draft",0)
																	.eq(mailViewDTO.getJunk()!=null && mailViewDTO.getJunk()==1,"junk",1)
																	.eq(mailViewDTO.getJunk()!=null && mailViewDTO.getJunk()==0,"junk",0)
		                                                            .like(mailViewDTO.getTheme()!=null,"theme",
				                                                            mailViewDTO.getTheme())
		                                                            .eq("owner_id",mailViewDTO.getUserId());
		Page<Mail> mailPage=mailService.page(new Page<>(mailViewDTO.getPageNumber(),mailViewDTO.getPageSize()),

				mailQueryWrapper);
		List<Mail> mailList = mailPage.getRecords();
		List<MailViewVO> mailViewVOList = new ArrayList<>();
		for (Mail mail : mailList) {
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
										 .draft(mail.getDraft())
			                             // 如果邮件内容长度大于20，截取前20个字符，否则取全部内容
					                     .Summary(mail.getContent().length() > 20 ? mail.getContent().substring(0, 20) : mail.getContent())
										 .junk(mail.getJunk())
			                             .build());
		}
		return Result.success(new PageResult(mailPage.getTotal(), mailViewVOList));
	}


	@Operation(summary = "批量删除邮件")
	@RequestMapping(value = "/mail/delete", method = RequestMethod.DELETE)
	@CrossOrigin
	public Result<?> deleteBatch(@RequestParam List<Integer> ids) {
		// 删除邮件之前判断junk字段是否为1
		for (int id : ids) {
			Mail mail = mailService.getById(id);
			if (mail.getJunk() == 1) {
				mailService.removeById(id);
			}else{
				mail.setJunk(1);
				mailService.updateById(mail);
			}
		}
		return Result.success();
	}
	
	
	@Operation(summary="批量星标邮件")
	@RequestMapping(value="/mail/star", method=RequestMethod.PUT)
	@CrossOrigin
	public Result<?> starBatch(@RequestParam List<Integer> ids)
	{
		Mail mail;
		log.info("ids = " + ids);
		for(int id: ids)
		{
			mail=mailService.getById(id);
			mail.setStar(1);
			log.warn("mail star = " + mail.getStar());
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

	@Operation(summary = "邮件详情")
	@RequestMapping(value = "/mail/maildetails/{mailId}", method = RequestMethod.GET)
	@CrossOrigin
	public Result<MailDetailsDTO> mailDetails(@PathVariable Integer mailId) {
		Mail mail = mailService.getById(mailId);
		if (mail == null) {
			return Result.error("邮件不存在");
		}
		MailDetailsDTO mailDetails = new MailDetailsDTO();
		BeanUtils.copyProperties(mail, mailDetails);
		User sender = userService.getById(mail.getSenderId());
		User receiver = userService.getById(mail.getReceiverId());
		mailDetails.setSenderName(sender.getUsername());
		mailDetails.setReceiverName(receiver.getUsername());
		mailDetails.setCcEmailAddresses(mail.getCcEmailAddresses());
		// 获取附件信息
		if (mail.getAttachmentIds() != null && !mail.getAttachmentIds().isEmpty()) {
			String[] attachmentIds = mail.getAttachmentIds().split(",");
			List<Integer> ids = Arrays.stream(attachmentIds).map(Integer::parseInt).collect(Collectors.toList());
			List<Attachment> attachments = attachmentService.listByIds(ids);
			log.info("attachments = " + attachments);
			// 转换附件大小的单位为MB
			for (Attachment attachment : attachments) {
				attachment.setSize(attachment.getSize() / 1024);
			}
			mailDetails.setAttachments(attachments);
		}
		return Result.success(mailDetails);
	}

	@Operation(summary = "切换已读/未读状态")
	@RequestMapping(value = "/mail/read/{mailId}", method = RequestMethod.PUT)
	@CrossOrigin
	public Result<?> read(@PathVariable Integer mailId) {
		Mail mail = mailService.getById(mailId);
		if (mail == null) {
			return Result.error("邮件不存在");
		}
		if (mail.getReadis() == 0) {
			mail.setReadis(1);
		} else {
			mail.setReadis(0);
		}
		mailService.updateById(mail);
		return Result.success();
	}
}
