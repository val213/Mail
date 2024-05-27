package com.example.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.Mail;
import com.example.backend.mapper.MailMapper;
import com.example.backend.service.MailService;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl extends ServiceImpl<MailMapper,Mail> implements MailService
{}
