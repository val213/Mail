package com.example.backend.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 后端统一返回结果
 *
 * @param <T>
 */
@Data
@NoArgsConstructor
public class Result<T> implements Serializable
{
	@Schema(title="响应码，0失败1成功")
	private Integer code;
	@Schema(title="响应码为0时会携带错误消息，前端将其用显示在页面上")
	private String message;
	@Schema(title="响应码为1且执行查询数据操作时data会携带响应数据")
	private T data;
	
	
	public static <T> Result<T> success()
	{
		Result<T> result=new Result<T>();
		result.code=1;
		return result;
	}
	
	
	public static <T> Result<T> success(T data)
	{
		Result<T> result=new Result<T>();
		result.setCode(1);
		result.setData(data);
		return result;
	}
	
	
	public static <T> Result<T> error(String message)
	{
		Result<T> result=new Result<>();
		result.setCode(0);
		result.setMessage(message);
		return result;
	}
}

