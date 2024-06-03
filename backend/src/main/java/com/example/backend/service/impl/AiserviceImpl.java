package com.example.backend.service.impl;

import com.example.backend.service.AiService;
import com.example.backend.utils.AiClientUtil;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import com.example.backend.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AiserviceImpl implements AiService {

    AiClientUtil aiClientUtil = new AiClientUtil();

    public final Result<String> chatWithMessage(String message){
        String answer;
        List<Object> messages = new ArrayList<>();
        messages.add(message);
        ModelApiResponse aiResponse = aiClientUtil.chatAiWithMessages(messages);
        answer = (String) aiResponse.getData().getChoices().get(2).getMessage().getContent();
        return Result.success(answer);
    }

    public final Result<String> chatWithMessages(List<String> messages){
        List<Object> chatMessages = new ArrayList<>(messages);
        ModelApiResponse aiResponse = aiClientUtil.chatAiWithMessages(chatMessages);
        String answer = (String) aiResponse.getData().getChoices().get(0).getMessage().getContent();
        return Result.success(answer);
    }

}
