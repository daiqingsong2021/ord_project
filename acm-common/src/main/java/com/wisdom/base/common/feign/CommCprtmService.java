package com.wisdom.base.common.feign;

import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.form.PlanCprtmAssignAddForm;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "acm-module-plan",configuration = FeignConfiguration.class)
public interface CommCprtmService {

    @RequestMapping(value = "/cprtm/assign",method = RequestMethod.POST)
    ApiResult assignCprtm(@RequestBody List<PlanCprtmAssignAddForm> reqList);

    @RequestMapping(value = "/cprtm/update",method = RequestMethod.POST)
    ApiResult updateCprtm(@RequestBody List<PlanCprtmAssignAddForm> reqList);

}
