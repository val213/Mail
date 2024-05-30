package com.example.backend.service;

import com.example.backend.result.Result;

import java.util.List;

public interface AiService {

    Result<String> chatWithMessage(String Message);
    Result<String> chatWithMessages(List<String> messages);
}
