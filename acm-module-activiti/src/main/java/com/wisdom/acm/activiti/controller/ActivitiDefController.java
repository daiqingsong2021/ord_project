package com.wisdom.acm.activiti.controller;

import com.wisdom.acm.activiti.service.ActivitiDefService;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/activiti")
public class ActivitiDefController {

    @Autowired
    private ActivitiDefService activitiDefService;

    /**
     * 删除流程定义
     * @param deploymentId
     * @return
     */
    @PostMapping("/def/process/{deploymentId}/delete")
    public ApiResult deleteProcess(@PathVariable("deploymentId") String deploymentId){
        activitiDefService.deleteProcess(deploymentId);
        return ApiResult.success();
    }

}
