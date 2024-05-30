package com.example.backend.service.impl;

import com.example.backend.service.AiService;
import com.example.backend.utils.AiClientUtil;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import com.example.backend.result.Result;

import java.util.ArrayList;
import java.util.List;

public class AiserviceImpl implements AiService {

    public final Result<String> chatWithMessage(String message){
        String answer;
        List<Object> messages = new ArrayList<>();
        messages.add(message);
        ModelApiResponse aiResponse = AiClientUtil.chatAiWithMessages(messages);
        answer = (String) aiResponse.getData().getChoices().get(2).getMessage().getContent();
        return Result.success(answer);
    }

    public final Result<String> chatWithMessages(List<String> messages){
        Result<String> result = new Result<>();
        List<Object> chatMessages = new ArrayList<>(messages);
        ModelApiResponse aiResponse = AiClientUtil.chatAiWithMessages(chatMessages);
        String answer = (String) aiResponse.getData().getChoices().get(2).getMessage().getContent();
        return result.success(answer);
    }

}
