package com.example.backend.pojo;
import com.example.backend.enums.ResultCode;
import com.fasterxml.jackson.annotation.JsonInclude;


/**
 * 对于返回前端的数据的统一封装
 * 只会在认证授权阶段用到
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseResult <T>{
    /**
     * 状态码
     */
    private Integer status;
    /**
     * 提示信息，如果有错误时，前端可以获取该字段进行提示
     */
    private String msg;
    /**
     * 查询到的结果数据，
     */
    private T data;
    private String token;
    public ResponseResult(Integer code, String msg) {
        this.status = code;
        this.msg = msg;
    }

    public ResponseResult(Integer code, T data) {
        this.status = code;
        this.data = data;
    }

    private ResponseResult(ResultCode resultCode) {
        this.status = resultCode.getHttpStatus();
        this.msg = resultCode.getMessage();
        this.data = null;
    }




    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ResponseResult(Integer code, String msg, T data) {
        this.status = code;
        this.msg = msg;
        this.data = data;
    }
    public ResponseResult(Integer code, String msg, String token) {
        this.status = code;
        this.msg = msg;
        this.token = token;
    }
}