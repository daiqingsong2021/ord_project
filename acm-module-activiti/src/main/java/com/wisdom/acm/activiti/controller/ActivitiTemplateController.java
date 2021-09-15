package com.wisdom.acm.activiti.controller;

import com.wisdom.acm.activiti.form.ActivitiTemplateAddForm;
import com.wisdom.acm.activiti.service.ActivitiTemplateService;
import com.wisdom.base.common.msg.ApiResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Author：wqd
 * Date：2020-03-30 9:18
 * Description：<描述>
 */

/**
 * 流程定义中 增加工作流流程处理人模板
 */
@RestController
@RequestMapping("activitiTemplate")
public class ActivitiTemplateController {
    private static final Logger logger = LoggerFactory.getLogger(ActivitiTemplateController.class);

    @Autowired
    ActivitiTemplateService activitiTemplateService;

    @PostMapping("/addActivitiTemplate")
    public ApiResult addActivitiTemplate(@RequestBody @Valid ActivitiTemplateAddForm activitiTemplateAddForm){
        activitiTemplateService.addActivitiTemplate(activitiTemplateAddForm);
        return ApiResult.success();
    }

    @RequestMapping("/getActivitiTemplate")
    @ResponseBody
    public ApiResult getActivitiTemplate(String activitiId){
        return ApiResult.success(activitiTemplateService.getActivitiTemplate(activitiId));
    }

    /*
     * zll根据流程节点id查询 for activiti
     * @return
     */
    @PostMapping("/queryActivitiByIds")
    public ApiResult<Map<String, String>> queryActivitiByIds(@RequestBody List<String> activitiIds) {
        Map<String, String> queryActivitiByIds = activitiTemplateService.queryActivitiByIds(activitiIds);
        return ApiResult.success(queryActivitiByIds);
    }

}
