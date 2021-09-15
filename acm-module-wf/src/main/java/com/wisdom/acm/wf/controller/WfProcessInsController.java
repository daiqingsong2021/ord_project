package com.wisdom.acm.wf.controller;

import com.wisdom.acm.wf.service.WfFormDataService;
import com.wisdom.acm.wf.service.WfProcessInsService;
import com.wisdom.acm.wf.vo.WfInstVo;
import com.wisdom.base.common.aspect.AddLog;
import com.wisdom.base.common.controller.BaseController;
import com.wisdom.base.common.enums.LoggerModuleEnum;
import com.wisdom.base.common.form.*;
import com.wisdom.base.common.log.AcmLogger;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.util.BaseUtil;
import com.wisdom.base.common.util.FormatUtil;
import com.wisdom.base.common.vo.sys.UserVo;
import com.wisdom.base.common.vo.wf.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 流程实例
 */
@RestController
@Api(tags = "流程实例服务")
public class WfProcessInsController extends BaseController {

	/**
	 * 流程实例服务
	 */
	@Autowired
	private WfProcessInsService processInsService;

	/**
	 * 流程审批数据服务
	 */
	@Autowired
	private WfFormDataService wfFormDataService;

	@ApiOperation(value = "首节点发起工作流选择后续参与者")
	@PostMapping("/process/start/next/{bizType}/{procDefKey}/candidate")
	public ApiResult<WfCandidateVo> getStartNextCandidate(
		@PathVariable("bizType") @ApiParam(value = "流程业务类型", required = true) String bizType,
		@PathVariable("procDefKey") @ApiParam(value = "流程定义key", required = true) String procDefKey,
		@RequestBody @ApiParam(value = "发起表单", required = true) WfStartProcessForm form) {
		String userId = FormatUtil.toString(BaseUtil.getLoginUser().getId()); // 获取用户
		WfCandidateVo candidate = this.processInsService.getStartNextCandidate(userId, bizType, procDefKey, form);
		return ApiResult.success(candidate);
	}

	@ApiOperation(value = "首节点发起工作流选择后续参与者")
	@PostMapping("/process/start/next/{bizType}/{procDefKey}/candidate/tree")
	public ApiResult<List<WfCandidateTreeVo>> getStartNextCandidateTree(
		@PathVariable("bizType") @ApiParam(value = "流程业务类型", required = true) String bizType,
		@PathVariable("procDefKey") @ApiParam(value = "流程定义key", required = true) String procDefKey,
		@RequestBody @ApiParam(value = "发起表单", required = true) WfStartProcessForm form) {
		String userId = FormatUtil.toString(BaseUtil.getLoginUser().getId()); // 获取用户
		List<WfCandidateTreeVo> candidates = this.processInsService.getStartNextCandidateTree(userId, bizType, procDefKey, form);
		return ApiResult.success(candidates);
	}

	@ApiOperation(value = "得到后续活动参与者")
	@GetMapping("/process/next/{taskId}/candidate")
	public ApiResult<WfCandidateVo> getNextCandidate(
		@PathVariable("taskId") @ApiParam(value = "任务ID", required = true) String taskId) {
		String userId = FormatUtil.toString(BaseUtil.getLoginUser().getId());// 获取用户
		WfCandidateVo candidates = this.processInsService.getNextCandidate(userId, taskId);
		return ApiResult.success(candidates);
	}

	@ApiOperation(value = "得到后续活动参与者树形")
	@GetMapping("/process/next/{taskId}/candidate/tree")
	public ApiResult<List<WfCandidateTreeVo>> getNextCandidateTree(
		@PathVariable("taskId") @ApiParam(value = "任务ID", required = true) String taskId) {
		String userId = FormatUtil.toString(BaseUtil.getLoginUser().getId());// 获取用户
		List<WfCandidateTreeVo> candidates = this.processInsService.getNextCandidateTree(userId, taskId);
		return ApiResult.success(candidates);
	}

	@ApiOperation(value = "到能驳回的活动")
	@GetMapping("/process/activity/{taskId}/reject")
	public ApiResult<List<WfActivityVo>> getRejectActivity(
		@PathVariable("taskId") @ApiParam(value = "任务ID", required = true) String taskId) {
		List<WfActivityVo> activitys = this.processInsService.getRejectActivity(taskId);
		return ApiResult.success(activitys);
	}

	@ApiOperation(value = "发起工作流")
	@PostMapping("/process/start")
	@AddLog(title = "发起工作流", module = LoggerModuleEnum.NONE)
	public ApiResult<WfRunProcessVo> startProcess(
		@RequestBody @ApiParam(value = "发起表单", required = true) WfStartProcessForm form) {
		UserVo user = BaseUtil.getLoginUser(); // 获取用户
		form.setUserId(FormatUtil.toString(user.getId()));
		this.setAcmLogger(new AcmLogger(FormatUtil.toString(user.getName()) + "发起工作流,流程名称为【" + form.getTitle() + "】"));
		WfRunProcessVo procVo = this.processInsService.startProcess(form); // 发起流程
		return ApiResult.success(procVo);
	}

	@ApiOperation(value = "认领任务")
	@PostMapping("/process/task/claim")
	@AddLog(title = "认领工作项任务", module = LoggerModuleEnum.NONE)
	public ApiResult<WfClaimVo> claimTask(
		@RequestBody @ApiParam(value = "认领表单", required = true) WfClaimTaskForm form) {
		UserVo user = BaseUtil.getLoginUser(); // 获取用户
		form.setUserId(FormatUtil.toString(user.getId()));
		WfTaskVo task = this.processInsService.getTaskByTaskId(form.getTaskId());
		this.setAcmLogger(new AcmLogger(user.getName() + "认领工作项任务,任务名称为【 " + this.getTaskName(task) + "】"));
		WfClaimVo claimVo = this.processInsService.claimTask(form);
		return ApiResult.success(claimVo);
	}

	@ApiOperation(value = "完成工作项任务")
	@PostMapping("/process/task/complete")
	@AddLog(title = "完成工作项任务", module = LoggerModuleEnum.NONE)
	public ApiResult<WfRunProcessVo> completeTask(
		@RequestBody @ApiParam(value = "工作项任务表单", required = true) WfCompleteTaskForm form) {
		UserVo user = BaseUtil.getLoginUser(); // 获取用户
		form.setUserId(FormatUtil.toString(user.getId()));
		WfTaskVo task = this.processInsService.getTaskByTaskId(form.getTaskId());
		this.setAcmLogger(new AcmLogger(user.getName() + "完成工作项任务,任务名称为【" + this.getTaskName(task) + "】"));
		WfRunProcessVo procVo = this.processInsService.completeTask(form, task);
		return ApiResult.success(procVo);
	}

	@ApiOperation(value = "撤销工作项任务")
	@PostMapping("/process/task/cancel")
	@AddLog(title = "撤销工作项任务", module = LoggerModuleEnum.NONE)
	public ApiResult<WfRunProcessVo> cancelTask(
		@RequestBody @ApiParam(value = "撤销任务表单", required = true) WfRejectTaskForm form) {
		UserVo user = BaseUtil.getLoginUser(); // 获取用户
		form.setUserId(FormatUtil.toString(user.getId()));
		WfTaskVo task = this.processInsService.getTaskByTaskId(form.getTaskId());
		this.setAcmLogger(new AcmLogger(user.getName() + "撤销工作项任务,任务名称为【" + this.getTaskName(task) + "】"));
		WfRunProcessVo procVo = this.processInsService.cancelTask(form, task);
		return ApiResult.success(procVo);
	}

	@ApiOperation(value = "驳回工作项任务")
	@PostMapping("/process/task/reject")
	@AddLog(title = "驳回工作项任务", module = LoggerModuleEnum.NONE)
	public ApiResult<WfRunProcessVo> rejectTask(
		@RequestBody @ApiParam(value = "驳回任务表单", required = true) WfRejectTaskForm form) {
		UserVo user = BaseUtil.getLoginUser(); // 获取用户
		form.setUserId(FormatUtil.toString(user.getId()));
		WfTaskVo task = this.processInsService.getTaskByTaskId(form.getTaskId());
		this.setAcmLogger(new AcmLogger(user.getName() + "驳回工作项任务,任务名称为【" + this.getTaskName(task) + "】"));
		WfRunProcessVo procVo = this.processInsService.rejectTask(form, task);
		return ApiResult.success(procVo);
	}

	@ApiOperation(value = "终止流程实例")
	@PostMapping("/process/terminate")
	@AddLog(title = "终止流程实例", module = LoggerModuleEnum.NONE)
	public ApiResult terminateProcess(
		@RequestBody @ApiParam(value = "终止流程表单", required = true) WfTerminateTaskForm form) {
		UserVo user = BaseUtil.getLoginUser(); // 获取用户
		form.setUserId(FormatUtil.toString(user.getId()));
		WfTaskVo task = this.processInsService.getTaskByTaskId(form.getTaskId());
		this.setAcmLogger(new AcmLogger(user.getName() + "终止流程实例,流程名称为【" + this.getProcInstName(form.getProcInstId()) + "】"));
		this.processInsService.terminateProcess(form, task); // 终止流程
		return ApiResult.success();
	}

	@ApiOperation(value = "删除流程实例")
	@DeleteMapping("/process/{procInstId}/delete")
	@AddLog(title = "删除流程实例", module = LoggerModuleEnum.NONE)
	public ApiResult deleteProcess(
		@PathVariable("procInstId") @ApiParam(value = "流程实例ID", required = true) String procInstId) {
		UserVo user = BaseUtil.getLoginUser(); // 获取用户
		this.setAcmLogger(new AcmLogger(user.getName() + "删除流程实例,流程名称为【" + this.getProcInstName(procInstId) + "】"));
		this.processInsService.deleteProcess(procInstId); // 删除流程
		return ApiResult.success();
	}

	@ApiOperation(value = "根据业务查询流程信息")
	@GetMapping("/process/{bizType}/{bizId}/list")
	public ApiResult<List<WfProcLogVo>> queryWfProcessList(
		@PathVariable("bizId") @ApiParam(value = "业务ID", required = true) Integer bizId,
		@PathVariable("bizType") @ApiParam(value = "业务类型", required = true) String bizType) {
		List<WfProcLogVo> wfProcessVos = this.processInsService.queryWfProcessLists(Arrays.asList(new Integer[]{bizId}), bizType);
		return ApiResult.success(wfProcessVos);
	}

	@ApiOperation(value = "根据业务查询流程信息")
	@PostMapping("/process/{bizType}/list")
	public ApiResult<List<WfProcLogVo>> queryWfProcessList(
		@RequestBody @ApiParam(value = "业务ID", required = true) List<Integer> bizIds,
		@PathVariable("bizType") @ApiParam(value = "业务类型", required = true) String bizType) {
		if (ObjectUtils.isEmpty(bizIds)) {
			return new ApiResult();
		}
		List<WfProcLogVo> wfProcessVos = this.processInsService.queryWfProcessLists(bizIds, bizType);
		return ApiResult.success(wfProcessVos);
	}

	@ApiOperation(value = "查询流程进展日志")
	@GetMapping("/process/log/detail/{procInstId}/list")
	public ApiResult<WfLogDetailVo> queryWfLogDetailList(
		@PathVariable("procInstId") @ApiParam(value = "流程实例ID", required = true) String procInstId) {
		WfLogDetailVo wfLogDetailVo = this.processInsService.queryWfLogDetailList(procInstId);
		return ApiResult.success(wfLogDetailVo);
	}

	@ApiOperation(value = "查询流程业务配置信息")
	@GetMapping(value = "/process/inst/{procInstId}/info")
	public ApiResult<WfInstVo> getWfInstInfoByProcInstId(
		@PathVariable("procInstId") @ApiParam(value = "流程实例ID", required = true) String procInstId) {
		WfInstVo instVo = this.processInsService.getWfInstInfoByProcInstId(procInstId);
		return ApiResult.success(instVo);
	}

	/**
	 * 查询任务名称
	 *
	 * @param task 任务
	 * @return 任务名称
	 */
	private String getTaskName(WfTaskVo task) {
		if (!ObjectUtils.isEmpty(task)) {
			return task.getName();
		}
		return "";
	}

	/**
	 * 查询任务名称
	 *
	 * @param procInstId 流程实例ID
	 * @return 流程实例名称
	 */
	private String getProcInstName(String procInstId) {
		WfProcessInstVo vo = this.processInsService.getProcessInstanceById(procInstId);
		if (!ObjectUtils.isEmpty(vo)) {
			return vo.getProcInstName();
		}
		return "";
	}

	@ApiOperation(value = "据流程实例Id获取流程实例")
	@GetMapping(value = "/process/{processInstanceId}/processInstance")
	public ApiResult<WfProcessInstVo> getProcessInstanceById(
		@PathVariable("processInstanceId") @ApiParam(value = "流程实例ID", required = true) String processInstanceId) {
		WfProcessInstVo vo = this.processInsService.getProcessInstanceById(processInstanceId);
		return ApiResult.success(vo);
	}

	@ApiOperation(value = "根据流程实例Id获取流程活动实例")
	@GetMapping(value = "/process/{activityInstanceId}/activityInstance")
	public ApiResult<WfActivityInstanceVo> getActivityInstanceByActivityInstanceId(
		@PathVariable("activityInstanceId") @ApiParam(value = "活动实例id", required = true) String activityInstanceId) {
		WfActivityInstanceVo vo = this.processInsService.getActivityInstanceByActivityInstanceId(activityInstanceId);
		return ApiResult.success(vo);
	}

	@ApiOperation(value = "根据流程实例Id获取流程活动实例")
	@GetMapping(value = "/process/{processInstanceId}/activityInstance/list")
	public ApiResult<List<WfActivityInstanceVo>> getActivityInstanceByProcessInstanceId(
		@PathVariable("processInstanceId") @ApiParam(value = "流程实例id", required = true) String processInstanceId) {
		List<WfActivityInstanceVo> vos = this.processInsService.getActivityInstanceByProcessInstanceId(processInstanceId);
		return ApiResult.success(vos);
	}

	@ApiOperation(value = "根据工作项id获得工作项")
	@GetMapping(value = "/process/{taskId}/task")
	public ApiResult<WfTaskVo> getTaskByTaskId(
		@PathVariable("taskId") @ApiParam(value = "任务ID", required = true) String taskId) {
		WfTaskVo vo = this.processInsService.getTaskByTaskId(taskId);
		return ApiResult.success(vo);
	}

	@ApiOperation(value = "查询流程实例ID的所有工作项")
	@GetMapping(value = "/process/{processInstanceId}/task/list")
	public ApiResult<List<WfTaskVo>> getTaskByProcessInstanceId(
		@PathVariable("processInstanceId") @ApiParam(value = "流程实例ID", required = true) String processInstanceId) {
		List<WfTaskVo> vos = this.processInsService.getTaskByProcessInstanceId(processInstanceId);
		return ApiResult.success(vos);
	}

	/**
	 * 根据流程实例ID查询指定用户的工作项
	 *
	 * @param processInstanceId 流程ID
	 * @return 任务
	 * @oaran userId 用户ID
	 */
	@ApiOperation(value = "根据流程实例ID查询指定用户的工作项")
	@GetMapping(value = "/process/{processInstanceId}/user/{userId}/run/task/list")
	public ApiResult<List<WfRunTaskVo>> getRunningTaskByProcessInstanceId(
		@PathVariable("processInstanceId") @ApiParam(value = "流程实例ID", required = true) String processInstanceId,
		@PathVariable("userId") @ApiParam(value = "用户ID", required = true) String userId) {
		List<WfRunTaskVo> vos = this.processInsService.getRunTaskByProcessInstanceId(processInstanceId, userId);
		return ApiResult.success(vos);
	}

	@ApiOperation(value = "根据业务类型和业务ID获取流程")
	@GetMapping(value = "/process/biz/{bizType}/{bizId}/processInstance")
	public ApiResult<WfProcessInstVo> getProcessInstanceByBizId(
		@PathVariable("bizType") @ApiParam(value = "业务类型ID", required = true) String bizType,
		@PathVariable("bizId") @ApiParam(value = "业务ID", required = true) Integer bizId) {
		WfFormProcVo form = this.wfFormDataService.queryProcInstIdByBiz(bizId, bizType);
		if (form != null) {
			return ApiResult.success(this.processInsService.getProcessInstanceById(form.getProcInstId()));
		} else {
			return ApiResult.success();
		}
	}

	@ApiOperation(value = "根据业务类型和业务ID获取流程活动实例")
	@GetMapping(value = "/process/biz/{bizType}/{bizId}/activityInstance/list")
	public ApiResult<List<WfActivityInstanceVo>> getActivityInstancesByBizId(
		@PathVariable("bizType") @ApiParam(value = "业务类型ID", required = true) String bizType,
		@PathVariable("bizId") @ApiParam(value = "业务id", required = true) Integer bizId) {
		WfFormProcVo form = this.wfFormDataService.queryProcInstIdByBiz(bizId, bizType);
		if (form != null) {
			return ApiResult.success(this.processInsService.getActivityInstanceByProcessInstanceId(form.getProcInstId()));
		} else {
			return ApiResult.success();
		}
	}

	@ApiOperation(value = "根据业务类型和业务ID获取流程所有工作项")
	@GetMapping(value = "/process/biz/{bizType}/{bizId}/task/list")
	public ApiResult<List<WfTaskVo>> getTasksByBizId(
		@PathVariable("bizType") @ApiParam(value = "业务类型ID", required = true) String bizType,
		@PathVariable("bizId") @ApiParam(value = "业务ID", required = true) Integer bizId) {
		WfFormProcVo form = this.wfFormDataService.queryProcInstIdByBiz(bizId, bizType);
		if (form != null) {
			return ApiResult.success(this.processInsService.getTaskByProcessInstanceId(form.getProcInstId()));
		} else {
			return ApiResult.success();
		}
	}

	@ApiOperation(value = "根据业务类型和业务ID获取流程指定用户的工作项")
	@GetMapping(value = "/process/biz/{bizType}/{bizId}/user/{userId}/run/task/list")
	public ApiResult<List<WfRunTaskVo>> getRunningTaskByBizId(
		@PathVariable("bizType") @ApiParam(value = "业务类型ID", required = true) String bizType,
		@PathVariable("bizId") @ApiParam(value = "业务ID", required = true) Integer bizId,
		@PathVariable("userId") @ApiParam(value = "用户ID", required = true) String userId) {
		WfFormProcVo form = this.wfFormDataService.queryProcInstIdByBiz(bizId, bizType);
		if (form != null) {
			return ApiResult.success(this.processInsService.getRunTaskByProcessInstanceId(form.getProcInstId(), userId));
		} else {
			return ApiResult.success();
		}
	}

}
