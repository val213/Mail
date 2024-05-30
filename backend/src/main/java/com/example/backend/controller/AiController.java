package com.example.backend.controller;

import com.example.backend.entity.Mail;
import com.example.backend.result.Result;
import com.example.backend.service.AiService;
import com.example.backend.service.MailAiService;
import com.example.backend.utils.AiClientUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.PrintStream;

public class AiController {
    @Autowired
    private MailAiService mailAiService;

    @PostMapping("ai/summarize")
    public Result<String> summarizeMailContent(@Valid@RequestBody Mail mail){
        String mailContent = mail.getContent();
        System.out.println(mailContent);
        Result<String> result = mailAiService.summarizeEmailContent(mailContent);
        return result;
    }

}