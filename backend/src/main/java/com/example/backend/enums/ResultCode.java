package com.example.backend.enums;

import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import java.text.DateFormat;

/**
 * 统一管理状态码
 *
 * @author zjh
 */
public enum ResultCode {
    /**
     * 自定义状态码
     */

    CONTINUE(100_000, "continue"),
    CHOOSE_IDENTITY(101_000,"请选择用户身份"),
    DESIGNER_MESSAGE(102_000,"请输入设计师信息"),
    PASSWORD_NOT_MODIFY(102_001,"请修改密码"),
    PASSWORD_TOO_WEAK(102_002,"密码强度太弱，请包含大小写英文字符，长度8-30位"),
    SUCCESS(200_000, "success"),
    NOT_VERIFIED(300_000, "该用户未经审核"),
    USER_HAD_NOT_BIND_OPENID(300_001,"用户未绑定一键登录"),
    OPENID_HAD_NOT_BIND_USER(300_002,"该微信未绑定用户"),
    USER_HAD_BIND_OPENID(300_001,"用户已经绑定过"),
    OPENID_HAD_BIND(300_002,"openid已被绑定过"),
    LOGIN_FAIL(302_000, "用户名或密码错误"),
    INVALID_TOKEN(302_001, "无效的token"),
    NOT_EXIST(303_000, "该用户不存在"),

    BAD_REQUEST_COMMON(400_000, "Bad Request"),
    NOT_SUPPORTED_FORMAT(400_001, "不支持文件格式"),
    PARAM_VALIDATION_ERROR(400_002, "无效的参数"),
    EMPTY_FILE(400_004, "存在上传文件为空"),
    IDENTITY_CHANGED(4000_005, "身份信息已改变，请重新登录"), // 前端跳转登录页面，必须唯一
    /**
     * 前端传来的时间字符串找不到匹配的正则模式时使用
     */
    INVALID_TIME_FORMAT(400_006, "无效的日期格式"),
    /**
     * 层级编码长度不符合当前业务上定义的长度时使用
     */
    WRONG_ENTITY_CODE_LENGTH(400_007, "层级编码长度不符合规范"),
    /**
     * 出现{@link java.io.EOFException}时使用，通常是在客户端本次请求断开了网络连接导致流异常中止时使用。
     */
    EOF_EXCEPTION(400_008, "文件传输异常中断"),
    /**
     * 出现{@link MultipartException}时使用，通常原因是"Current request is not a multipart request"，
     * 当客户端缺少必须上传的文件时使用。
     */
    MULTIPART_EXCEPTION(400_009, "无法解析文件，请检查是否正确上传文件"),
    /**
     * 客户端请求时缺少必须的用来表示原文件名的参数
     */
    MISSING_ORIGINAL_FILE_NAME(400_010, "缺少原文件名"),
    FILE_FORMATS_ARE_NOT_SUPPORTED(400_011, "不支持的文件格式"),
    AT_LEAST_ONE_FILE(400_011, "至少需要保留一个文件"),

    PASSWORD_MISMATCH(400_012,"密码匹配错误"),

    PERMISSION_DENIED(401_000, "您无权使用该身份新建提议"),
    OPERATION_DENIED(401_001, "您无权操作此提议"),
    CHECK_DENIED(401_002, "该申请已被审核"),
    REGISTER_DENIED(401_003, "请勿重复注册"),
    IDENTITY_DENIED(401_004, "请先注册有效身份"),
    GROUP_BROWSE_DENIED(401_005, "您无权查看该组织下的数据"),
    FILE_OPERATION_DENIED(401_006, "您无权操作该文件"),
    RELATE_ID_OPERATION_DENIED(401_007,"您无权将文件上传至该relateId下"),

    LOW_SECURITY_PASSWORD(402_001,"密码安全度过低"),
    VALID_OVERTIME(402_002,"验证码已失效"),

    FORBIDDEN_COMMON(403_000, "Forbidden"),
    FORBIDDEN_STAGED(403_001, "无法访问暂存内容"),
    FORBIDDEN_NOT_CHECK(403_002, "该内容还未审核"),
    FORBIDDEN_CHECK_FAILED(403_003, "该内容审核未通过"),
    NOT_MATCH_RECOVER_PARAM(403_004,"手机号或邮箱错误"),
    NOT_MATCH_RECOVER_CODE(403_005,"账号或认证码错误"),
    NOT_FOUND_IMAGE(404_001, "找不到该图片"),
    NOT_FOUND_ATTACHMENT(404_002, "找不到该附件"),
    NOT_FOUND_ENTITY_TYPE(404_003, "找不到该实体类型"),
    NOT_FOUND_CHARACTER_ROLE(404_004, "找不到该角色类型"),
    NOT_FOUND_GROUP_ENTITY(404_005, "找不到该组织机构"),
    NOT_FOUND_TYPE_ENTITY(404_006, "找不到该类型实体"),
    NOT_FOUND_DESIGNER_IDENTITY(404_007, "找不到该设计师"),
    NOT_FOUND_MANAGER_IDENTITY(404_008, "找不到该联络专员"),
    NOT_PERMISSIONS_SWITCH_ROLE(404_009, "没有权限切换到该角色"),

    /**
     * 出现{@link java.net.SocketTimeoutException}时使用
     */
    SOCKET_TIMEOUT_EXCEPTION(408_000, "网络连接超时"),

    /**
     * 出现{@link MaxUploadSizeExceededException}时使用
     */
    MAX_UPLOAD_SIZE_EXCEEDED_EXCEPTION(413_000, "上传文件过大"),

    SERVER_EXCEPTION(500_000, "服务器异常"),
    FILE_UPLOAD_ERROR(500_001, "文件上传过程中出现异常"),
    OPERATION_ERROR(500_002, "服务器出现运算错误"),
    CONVERT_ERROR(500_003, "数据格式转化出错"),
    FILE_DELETE_ERROR(500_004, "文件删除过程中出现异常"),
    CONFIRM_ERROR(500_005, "确认失败"),


    /**
     * 解析过程出现异常，如在使用{@link DateFormat#parse(String)}出现
     */
    PARSE_EXCEPTION(500_006, "解析过程出现异常"),
    SEND_MESSAGE_ERROR(500_007, "消息实体插入数据库失败"),

    DATA_ABERRANT(501_001, "数据库条目异常"),
    REDIS_UPLOAD_ERROR(501_002, "向redis写入数据时出现异常"),
    REDIS_DOWNLOAD_ERROR(501_003, "向redis读取数据时出现异常");


    private final Integer status;
    private final String message;

    ResultCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * @return 业务上定义的status code, 如400_000、400_001等
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @return 具体信息
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return 3位Http状态码，如404
     */
    public int getHttpStatus() {
        HttpStatus httpStatus = HttpStatus.valueOf(getStatus() / 1000);
        return httpStatus.value();
    }
}