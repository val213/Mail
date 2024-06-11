package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.Attachment;
import com.example.backend.entity.Mail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AttachmentMapper extends BaseMapper<Attachment> {
}
