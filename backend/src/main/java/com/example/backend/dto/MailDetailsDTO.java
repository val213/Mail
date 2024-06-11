package com.example.backend.dto;

import com.example.backend.entity.Attachment;
import lombok.Data;

import java.util.List;
@Data
public class MailDetailsDTO {
        private Integer id;
        private Integer senderId;
        private Integer receiverId;
        private List<String> cc;
        private List<String> bcc;
        private String theme;
        private String content;
        private String sendTime;
        private List<Attachment> attachments;

}
