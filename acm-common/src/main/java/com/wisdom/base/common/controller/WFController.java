package com.wisdom.base.common.controller;

import com.wisdom.base.common.form.WfRuningProcessForm;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.wf.WfCandidateVo;
import com.wisdom.base.common.vo.wf.WfRunProcessVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@Api(tags="流程回调事件服务")
public class WFController {

    @ApiOperation(value="自定义流程参与者<WfRunProcessVo.setCandidate(new WfCandidateVo().setActivities(null))>清除参与者")
    @ApiImplicitParams({@ApiImplicitParam(name="form",value= "流程运行表单")})
    @PostMapping(value = "/wf/custom/workflow/candidate")
    public ApiResult<WfRunProcessVo> customWorkFlowCandidate(@RequestBody WfRuningProcessForm form) {
        WfRunProcessVo vo = new WfRunProcessVo();
        //vo.setCandidate(new WfCandidateVo().setActivities(null)); //清除本次参与者
        //把业务数据的状态变更为审批中.
        return ApiResult.success(vo);
    }

    @ApiOperation(value="发起流程后事件")
    @ApiImplicitParams({@ApiImplicitParam(name="form",value= "流程运行表单")})
    @PostMapping(value = "/wf/start/workflow/after")
    public ApiResult<WfRunProcessVo> startWorkFlowAfter(@RequestBody WfRuningProcessForm form) {
        //把业务数据的状态变更为审批中.
        return ApiResult.success();
    }

    @ApiOperation(value="完成流程事件后")
    @ApiImplicitParams({@ApiImplicitParam(name="form",value= "流程运行表单")})
    @PostMapping(value = "/wf/complete/workflow/after")
    public ApiResult<WfRunProcessVo> completeWorkFlowAfter(@RequestBody WfRuningProcessForm form) {
        //把业务数据的状态变更为已批准.
        return ApiResult.success();
    }

    @ApiOperation(value="终止流程事件后")
    @ApiImplicitParams({@ApiImplicitParam(name="form",value= "流程运行表单")})
    @PostMapping(value = "/wf/terminate/workflow/after")
    public ApiResult<WfRunProcessVo> terminateWorkFlowAfter(@RequestBody WfRuningProcessForm form) {
        //把业务数据的状态变更为编制中.
        return ApiResult.success();
    }

    @ApiOperation(value="终止流程事件前")
    @ApiImplicitParams({@ApiImplicitParam(name="form",value= "流程运行表单")})
    @PostMapping(value = "/wf/terminate/workflow/before")
    public ApiResult<WfRunProcessVo> terminateWorkFlowBefore(@RequestBody WfRuningProcessForm form) {
        return ApiResult.success();
    }

    @ApiOperation(value="删除流程事件后")
    @ApiImplicitParams({@ApiImplicitParam(name="form",value= "流程运行表单")})
    @PostMapping(value = "/wf/delete/workflow/after")
    public ApiResult<WfRunProcessVo> deleteWorkFlowAfter(@RequestBody WfRuningProcessForm form) {
        //把业务数据的状态变更为编制中.
        return ApiResult.success();
    }

    @ApiOperation(value="删除流程事件前")
    @ApiImplicitParams({@ApiImplicitParam(name="form",value= "流程运行表单")})
    @PostMapping(value = "/wf/delete/workflow/before")
    public ApiResult<WfRunProcessVo> deleteWorkFlowBefore(@RequestBody WfRuningProcessForm form) {
        return ApiResult.success();
    }

    @ApiOperation(value="执行工作项事件后")
    @ApiImplicitParams({@ApiImplicitParam(name="form",value= "流程运行表单")})
    @PostMapping(value = "/wf/execute/task/after")
    public ApiResult<WfRunProcessVo> executeTaskAfter(@RequestBody WfRuningProcessForm form) {
        return ApiResult.success();
    }

    @ApiOperation(value="执行工作项事件前(可设置流程业务变量)")
    @ApiImplicitParams({@ApiImplicitParam(name="form",value= "流程运行表单")})
    @PostMapping(value = "/wf/execute/task/before")
    public ApiResult<WfRunProcessVo> executeTaskBefore(@RequestBody WfRuningProcessForm form) {
        WfRunProcessVo vo = new WfRunProcessVo();
        Map<String, Object> vars = new LinkedHashMap<>(); //流程变量，全局可用，所有活动ID作为条件时，不能有"-"
        //vars.put("sid_2F63285F_A5A7_4562_8442_6BE6C2796F80", true);
        vo.setVars(vars);
        return ApiResult.success(vo);
    }

    @ApiOperation(value="驳回活动事件后")
    @ApiImplicitParams({@ApiImplicitParam(name="form",value= "流程运行表单")})
    @PostMapping(value = "/wf/back/activity/after")
    public ApiResult<WfRunProcessVo> backActivityAfter(@RequestBody WfRuningProcessForm form) {
        return ApiResult.success();
    }

    @ApiOperation(value="驳回活动事件前")
    @ApiImplicitParams({@ApiImplicitParam(name="form",value= "流程运行表单")})
    @PostMapping(value = "/wf/back/activity/before")
    public ApiResult<WfRunProcessVo> backActivityBefore(@RequestBody WfRuningProcessForm form) {
        return ApiResult.success();
    }

}
