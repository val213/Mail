package com.example.backend.entity;
import com.baomidou.mybatisplus.annotation.TableField;
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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("attachment")
public class Attachment implements Serializable {
    @TableId(value = "id")
    private Integer id; // 附件的唯一标识符
    @TableField("file_name")
    private String fileName; // 附件的文件名
    @TableField("mime_type")
    private String mimeType; // 附件的MIME类型
    @TableField("file_size")
    private Long size; // 附件的大小（字节）
    @TableField("created_at")
    private LocalDateTime createdAt; // 创建时间
    @TableField("download_url")
    private String downloadUrl; // 附件的下载链接
    // 全参构造函数
    public Attachment(String fileName, byte[] data, String mimeType) {
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.createdAt = LocalDateTime.now(); // 设置创建时间
        this.size = (long) data.length; // 设置附件大小
        this.downloadUrl = "/attachments/" + fileName; // 设置下载链接
    }
}