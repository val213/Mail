package com.example.backend.service;

public interface VerificationService {
    String sendVerificationCode(String phoneNumber);
    boolean verifyCode(String phoneNumber, String code);
}
