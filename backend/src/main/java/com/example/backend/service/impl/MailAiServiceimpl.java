package com.example.backend.service.impl;

import com.example.backend.result.Result;
import com.example.backend.service.MailAiService;
import com.example.backend.utils.AiClientUtil;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MailAiServiceimpl implements MailAiService {

    AiClientUtil aiClientUtil = new AiClientUtil();

    public MailAiServiceimpl(){};

    public Result<String> summarizeEmailContent(String emailContent){
        Result<String> result = new Result<>();
        List<Object> messages = new ArrayList<>();
        Object message1 = "你是邮件内容总结助手，请根据我提供的邮件内容，总结邮件内容。";
        String message2 = "需要总结的邮件内容是： " + emailContent;
        messages.add(message1);
        messages.add(message2);
        ModelApiResponse aiResponse = aiClientUtil.chatAiWithMessages(messages);
        String answer = (String) aiResponse.getData().getChoices().get(0).getMessage().getContent();
        return result.success(answer);
    }
}
