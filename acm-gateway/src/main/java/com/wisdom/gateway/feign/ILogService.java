package com.wisdom.gateway.feign;

import com.wisdom.base.common.log.LogInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 */
@FeignClient("acm-module-sys")
public interface ILogService {

	@RequestMapping(value = "/api/log/save", method = RequestMethod.POST)
	public void saveLog(LogInfo info);

}
