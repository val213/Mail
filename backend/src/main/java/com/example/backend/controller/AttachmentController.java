package com.example.backend.controller;

import com.example.backend.service.AttachmentService;
import com.example.backend.service.UserService;
import com.example.backend.utils.AliOssUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/attach")
public class AttachmentController {
    private  AttachmentService attachmentService;
    private  AliOssUtil aliOssUtil;
    private  UserService userService;

    /**
     * 上传附件
     */
    @Operation(summary="上传附件")
    @PostMapping("/attach/upload")
    public void upload() {
    }

    /**
     * 删除附件
     */
    @Operation(summary="删除附件")
    @PostMapping("/attach/delete")
    public void delete() {
    }

    /**
     * 下载附件
     */
    @Operation(summary="下载附件")
    @PostMapping("/attach/download/{attachmentId}")
    public void download(@PathVariable String attachmentId) {

    }
    /**
     * 查看附件
     */
    @Operation(summary="查看附件")
    @PostMapping("/attach/view")
    public void view() {
    }
    /**
     * 查看所有附件
     */
    @Operation(summary="查看所有附件")
    @PostMapping("/attach/viewAll")
    public void viewAll() {
    }
}
