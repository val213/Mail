package com.example.backend.controller;

import com.example.backend.config.SecurityConfig;
import com.example.backend.entity.Mail;
import com.example.backend.result.Result;
import com.example.backend.service.AiService;
import com.example.backend.service.MailAiService;
import com.example.backend.service.impl.MailAiServiceimpl;
import com.example.backend.utils.AiClientUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiController extends SecurityConfig {

    MailAiService mailAiService = new MailAiServiceimpl();

    public AiController(){};

    @PostMapping("/ai/summarize")
    public Result<String> summarizeMailContent(@Valid@RequestBody String mailContent){
        System.out.println(mailContent);
        Result<String> result = mailAiService.summarizeEmailContent(mailContent);
        return result;
    }

}
