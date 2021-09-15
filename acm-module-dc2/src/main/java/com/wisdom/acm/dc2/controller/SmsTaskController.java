package com.wisdom.acm.dc2.controller;

import com.wisdom.acm.dc2.form.SmsTaskAddForm;
import com.wisdom.acm.dc2.service.SmsTaskService;
import com.wisdom.base.common.msg.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

//短信任务
@RestController
@RequestMapping(value = "sms/task")
public class SmsTaskController {

    @Autowired
    private SmsTaskService smsTaskService;

    /**
     * 新增
     * @param form 基本信息表单
     * @return
     */
    @PostMapping(value = "/insert")
    public ApiResult insert(@RequestBody @Valid SmsTaskAddForm form) {
        smsTaskService.addSmsTask(form);
        return ApiResult.success();
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @DeleteMapping(value = "/del")
    public ApiResult del(@RequestBody List<Integer> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return ApiResult.result(1001, "ids不能为空");
        }
        smsTaskService.delSmsTask(ids);
        return ApiResult.success();
    }

    /**
     * 更新状态
     * @param ids
     * @return
     */
    @PutMapping(value = "/updateStatus/{status}")
    public ApiResult updateStatus(@RequestBody List<Integer> ids,@PathVariable(value = "status")Integer status) {
        if (ObjectUtils.isEmpty(ids)) {
            return ApiResult.result(1001, "ids不能为空");
        }
        ids.stream().forEach(taskId ->{
            smsTaskService.updateSmsTaskStatus(taskId, status);
        });
        return ApiResult.success();
    }
}
