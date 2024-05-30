package com.example.backend.entity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("attachment")
public class Attachment implements Serializable {

    private int id; // 附件的唯一标识符

    private String fileName; // 附件的文件名

    private String mimeType; // 附件的MIME类型


    private Long size; // 附件的大小（字节）

    private LocalDateTime createdAt; // 创建时间

    public void setId(int id) {
        this.id = id;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // 全参构造函数
    public Attachment(String fileName, byte[] data, String mimeType) {
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.createdAt = LocalDateTime.now(); // 设置创建时间
    }
}