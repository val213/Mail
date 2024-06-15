package com.example.backend.dto;

import com.example.backend.entity.Attachment;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class MailDetailsDTO {
        private Integer id;
        private Integer senderId;
        private Integer receiverId;
        private String senderName;
        private String receiverName;
        private List<String> cc;
        private List<String> bcc;
        private String theme;
        private String content;
        private LocalDateTime sendTime;
        private List<Attachment> attachments;

}
