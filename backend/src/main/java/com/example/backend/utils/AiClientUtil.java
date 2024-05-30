package com.example.backend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Component
public class AiClientUtil {

    private final ClientV4 client;

    public AiClientUtil(){
        client = new ClientV4.Builder("d487eb7e062ace5cafa265abec9ccee3.eRBjDXHZ95qriwbE").build();
    }

    //提供同步调用服务，调用后可以一次性得到结果，如果需要调用请提前设计好对话过程
    public ModelApiResponse chatAiWithMessages(List<Object> messages){
        String answer = "";
        //创建ChatMessages列表
        List<ChatMessage> chatMessages = new ArrayList<>();
        for (int i = 0; i < messages.size(); i++){
            ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), messages.get(i));
            chatMessages.add(chatMessage);
        }
        String requestId = String.format("request-%d", System.currentTimeMillis());

        //编写prompt请求
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(Constants.ModelChatGLM4)
                .stream(Boolean.FALSE)
                .invokeMethod(Constants.invokeMethod)
                .messages(chatMessages)
                .requestId(requestId)
                .build();

        ModelApiResponse invokeModelApiResp = client.invokeModelApi(chatCompletionRequest);
        ObjectMapper mapper = new ObjectMapper();
        try {
            System.out.println("aimodel output: " + mapper.writeValueAsString(invokeModelApiResp));
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return invokeModelApiResp;
    }


}
