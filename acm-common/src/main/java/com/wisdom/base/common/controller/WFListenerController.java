package com.wisdom.base.common.controller;

import com.wisdom.base.common.form.WfRuningProcessForm;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.wf.WfActivityInstanceVo;
import com.wisdom.base.common.vo.wf.WfProcessInstVo;
import com.wisdom.base.common.vo.wf.WfRunProcessVo;
import com.wisdom.base.common.vo.wf.WfTaskListenerVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@Api(tags="流程监听事件服务")
public class WFListenerController {

	@ApiOperation(value="创建工作项事件")
	@ApiImplicitParams({@ApiImplicitParam(name="form",value= "流程运行表单")})
	@PostMapping(value = "/wf/listener/create/task")
	public ApiResult createTask(@RequestBody WfTaskListenerVo task) {
		//System.out.println("创建工作项事件createTask=" + task);
		return ApiResult.success();
	}

	@ApiOperation(value="完成工作项事件")
	@ApiImplicitParams({@ApiImplicitParam(name="form",value= "流程运行表单")})
	@PostMapping(value = "/wf/listener/complete/task")
	public ApiResult completeTask(@RequestBody WfTaskListenerVo task) {
		//System.out.println("完成工作项事件completeTask=" + task);
		return ApiResult.success();
	}

	@ApiOperation(value="完成流程事件")
	@ApiImplicitParams({@ApiImplicitParam(name="form",value= "流程运行表单")})
	@PostMapping(value = "/wf/listener/complete/workflow")
	public ApiResult completeWorkFlow(@RequestBody WfProcessInstVo procInst) {
		//System.out.println("完成流程事件completeWorkFlow=" + procInst);
		return ApiResult.success();
	}

	@ApiOperation(value="终止流程事件")
	@ApiImplicitParams({@ApiImplicitParam(name="form",value= "流程运行表单")})
	@PostMapping(value = "/wf/listener/terminate/workflow")
	public ApiResult terminateWorkFlow(@RequestBody WfProcessInstVo procInst) {
		//System.out.println("终止流程事件terminateWorkFlow=" + procInst);
		return ApiResult.success();
	}

	@ApiOperation(value="完成活动节点事件")
	@ApiImplicitParams({@ApiImplicitParam(name="form",value= "流程运行表单")})
	@PostMapping(value = "/wf/listener/complete/activity")
	public ApiResult completeActivity(@RequestBody WfActivityInstanceVo activity) {
		//System.out.println("完成活动节点事件completeActivity=" + activity);
		return ApiResult.success();
	}

}
