package com.wisdom.acm.wf.controller;

import com.wisdom.acm.wf.form.WfLogAddForm;
import com.wisdom.acm.wf.service.WfLogService;
import com.wisdom.acm.wf.vo.WfLogVo;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.wf.WfLogDetailVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 流程日志
 */
@Api(tags = "流程实例服务")
@RestController
public class WfLogController {

	@Autowired
	private WfLogService wfLogService;

	@ApiModelProperty(value = "查询流程日志")
	@GetMapping("/log/{procInstId}/list")
	public ApiResult<WfLogDetailVo> queryLogVoListByProcInstId(
		@PathVariable("procInstId") @ApiParam(value = "流程实例ID", required = true) String procInstId) {
		WfLogDetailVo logDetailVo = wfLogService.getLogVoListByProcInstId(procInstId);
		return ApiResult.success(logDetailVo);
	}

	@ApiModelProperty(value = "增加流程日志")
	@PostMapping("/log/add")
	public ApiResult<WfLogVo> addWfLog(
		@RequestBody @ApiParam(value = "流程增加日志", required = true) WfLogAddForm wfLogAddForm) {
		WfLogVo wfLogVo = wfLogService.addWfLog(wfLogAddForm);
		return ApiResult.success(wfLogVo);
	}
}
