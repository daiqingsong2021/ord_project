package com.wisdom.acm.wf.controller;

import com.wisdom.acm.wf.service.WfFormService;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class WfFormController {

    @Autowired
    private WfFormService wfFormService;

    @GetMapping("/form/{procInstId}")
    public ApiResult getProcCreatorByProcInstId(@PathVariable("procInstId") String procInstId){
       int creator =   wfFormService.getProcCreatorByProcInstId(procInstId);
       return ApiResult.success(creator);
    }

}
