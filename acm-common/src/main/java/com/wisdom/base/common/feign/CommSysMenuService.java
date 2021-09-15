package com.wisdom.base.common.feign;

import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zll
 * 2020/8/12/012 10:16
 * Description:<描述>
 */
@FeignClient(value = "acm-module-sys", configuration = FeignConfiguration.class)
public interface CommSysMenuService {
    /**
     * 查询menu名称  for activiti
     * @return
     */
    @GetMapping(value = "/menu/queryMenuNameByCode")
    public ApiResult queryMenuNameByCode(@RequestParam String menuCode);
}
