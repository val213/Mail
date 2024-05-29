package com.example.backend.utils;

import com.zhipu.oapi.ClientV4;
import com.zhipu.oapi.Constants;
import com.zhipu.oapi.service.v4.model.ChatMessage;
import com.zhipu.oapi.service.v4.model.ChatMessageRole;
import com.zhipu.oapi.service.v4.model.ChatCompletionRequest;
import com.zhipu.oapi.service.v4.model.ModelApiResponse;
import
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AiClientUtil {

    private static ClientV4 client = null;

    public AiClientUtil(){
        client = new ClientV4.Builder("{8aba98f13a2765c1e94837f45ab134e8.RlGDHsYSpdsQgY5D}").build();
    }

    //提供同步调用服务，调用后可以一次性得到结果
    public static void chatAiWithMessages(List<String> message){
        //创建ChatMessages列表
        List<ChatMessage> chatMessages = new ArrayList<>();
        ChatMessage chatMessage = new ChatMessage(ChatMessageRole.USER.value(), message);
        chatMessages.add(chatMessage);
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

        try {
            System.out.println("aimodel output: " + mapper);
        }
    }

}
