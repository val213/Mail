package com.example.backend.service;

import com.example.backend.result.Result;
import org.springframework.stereotype.Service;


@Service
public interface MailAiService {
    Result<String> summarizeEmailContent(String emailContent);
}
