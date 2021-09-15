package com.wisdom.acm.activiti.controller;

import com.wisdom.acm.activiti.util.Resp;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class GlobalController {

	/**
	 *统一异常处理
	 */
	@ExceptionHandler
	@ResponseBody
	public Object exception(Exception e) {
		return Resp.fail(e.getMessage());
	}
}
