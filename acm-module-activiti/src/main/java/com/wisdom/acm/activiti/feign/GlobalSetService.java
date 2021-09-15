package com.wisdom.acm.activiti.feign;

import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(value = "acm-module-base",configuration = FeignConfiguration.class)
public interface GlobalSetService {

    @GetMapping(value = "set/{boCode}/{bsKey}/info")
    public ApiResult<String> getSet(@PathVariable("boCode") String boCode, @PathVariable("bsKey") String bsKey);

}
