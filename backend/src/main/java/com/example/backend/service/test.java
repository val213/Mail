package com.example.backend.service;

import com.example.backend.result.Result;
import com.example.backend.service.impl.MailAiServiceimpl;

public class test {
    public static void main(String[] args){
        MailAiService mailAiService = new MailAiServiceimpl();
        Result<String> result;
        String mailcontent = "亲爱的团队，我写信是为了更新我们项目的进度。我们已经完成了初步的设计阶段，现在正在进入开发阶段。团队一直在努力工作，我相信我们会在即将到来的截止日期前完成任务。感谢你们的持续支持和辛勤工作。最好的祝愿，cjy";
        result = mailAiService.summarizeEmailContent(mailcontent);
        System.out.println(result.getData());
    }
}
