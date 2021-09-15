package com.wisdom.base.common.feign;

import com.wisdom.base.common.config.FeignConfiguration;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.wf.WfProcessBizVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "acm-module-wf",configuration = FeignConfiguration.class)
public interface CommWfFormService {

    /**
     * 通过流程实例查询bizid集合
     * @param procInstId
     * @return
     */
    @RequestMapping(value = "/form/data/{procInstId}/list", method = RequestMethod.GET)
    List<WfProcessBizVo> queryWfFormDataBizIds(@PathVariable("procInstId") Integer procInstId);


    @GetMapping("/form/{procInstId}")
    ApiResult<Integer> getProcCreatorByProcInstId(@PathVariable("procInstId") String procInstId);
}
