package com.wisdom.acm.activiti.util;

public class Resp {

	public static final int SUCCESS = 0;
	public static final int FAIL = -1;
	public static final int WARN = 1;

	public static final String CODE = "code";
	public static final String MSG = "message";
	public static final String DATA = "data";

	/**
	 * 代码
	 */
	private int code;

	/**
	 * 消息
	 */
	private String message;

	/**
	 * 返回数据
	 */
	private Object data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public static Resp succ() {
		return result(SUCCESS, null, null);
	}

	public static Resp succ(String message) {
		return result(SUCCESS, message, null);
	}

	public static Resp succ(String message, Object data) {
		return result(SUCCESS, message, data);
	}

	public static Resp fail(String message) {
		return result(FAIL, message, null);
	}

	public static Resp warn(String message) {
		return result(WARN, message, null);
	}

	public static Resp warn(String message, Object data) {
		return result(WARN, message, data);
	}

	public static Resp result(int code, String message, Object data) {
		Resp resp = new Resp();
		resp.setCode(code);
		resp.setMessage(message);
		resp.setData(data);
		return resp;
	}
}
