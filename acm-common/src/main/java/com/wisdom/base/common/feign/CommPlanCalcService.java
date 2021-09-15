package com.wisdom.base.common.feign;

import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.form.PlanCprtmAssignAddForm;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.util.calc.PmSchedule;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "acm-module-plan-calc",configuration = FeignConfiguration.class)
public interface CommPlanCalcService {

    @RequestMapping(value = "/calc/calculate",method = RequestMethod.POST)
    ApiResult<PmSchedule> calculate(@RequestBody PmSchedule schedule);

}
