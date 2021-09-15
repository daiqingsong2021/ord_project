package com.wisdom.acm.activiti.controller;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.activiti.service.ActivitiService;
import com.wisdom.base.common.form.*;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.vo.base.ActModelVo;
import com.wisdom.base.common.vo.wf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/activiti")
public class ActivitiController {

	@Autowired
	private ActivitiService activitiService;

	@Autowired
	private HttpServletRequest request;

	/**
	 * 首节点发起工作流选择后续参与者
	 *
	 * @param procDefKey 流程定义
	 * @return 后续参与者
	 */
	@PostMapping("/process/start/next/{procDefKey}/candidate")
	public ApiResult<WfCandidateVo> getStartNextCandidate(@PathVariable("procDefKey") String procDefKey, @RequestBody Map<String, Object> vars) {
		WfCandidateVo candidates = activitiService.getStartNextCandidate(procDefKey, vars);
		return ApiResult.success(candidates);
	}

	/**
	 * 得到后续活动参与者
	 *
	 * @param taskId 任务ID
	 * @return 后续参与者
	 */
	@GetMapping("/process/next/{taskId}/candidate")
	public ApiResult<WfCandidateVo> getNextCandidate(@PathVariable("taskId") String taskId) {
		WfCandidateVo candidates = activitiService.getNextCandidate(taskId);
		return ApiResult.success(candidates);
	}

	/**
	 * 到能驳回的活动
	 *
	 * @param taskId 任务ID
	 * @return 后续参与者
	 */
	@GetMapping("/process/activity/{taskId}/reject")
	public ApiResult<List<WfActivityVo>> getRejectActivity(@PathVariable("taskId") String taskId) {
		List<WfActivityVo> activitys = activitiService.getRejectActivity(taskId);
		return ApiResult.success(activitys);
	}

	/**
	 * 发起工作流程(业务)
	 *
	 * @param startProcessForm Form
	 * @return WfProcessInstVo
	 */
	@PostMapping("/process/start")
	public ApiResult<WfRunProcessVo> startProcess(@RequestBody WfStartProcessForm startProcessForm) {
		WfRunProcessVo procVo = activitiService.startProcess(startProcessForm);
		return ApiResult.success(procVo);
	}

	/**
	 * 认领任务
	 *
	 * @param claimTaskForm Form
	 * @return WfClaimVo
	 */
	@PostMapping("/process/task/claim")
	public ApiResult<WfClaimVo> claimTask(@RequestBody WfClaimTaskForm claimTaskForm) {
		WfClaimVo vo = activitiService.claimTask(claimTaskForm);
		return ApiResult.success(vo);
	}

	/**
	 * 完成任务
	 *
	 * @param completeTaskForm Form
	 * @return Boolean
	 */
	@PostMapping("/process/task/complete")
	public ApiResult<WfRunProcessVo> completeTask(@RequestBody WfCompleteTaskForm completeTaskForm) {
		WfRunProcessVo procVo = activitiService.completeTask(completeTaskForm);
		return ApiResult.success(procVo);
	}

	/**
	 * 撤销任务
	 *
	 * @param rejectTaskForm Form
	 * @return ApiResult
	 */
	@PostMapping("/process/task/cancel")
	public ApiResult<WfRunProcessVo> cancelTask(@RequestBody WfRejectTaskForm rejectTaskForm) {
		WfRunProcessVo procVo = activitiService.cancelTask(rejectTaskForm);
		return ApiResult.success(procVo);
	}

	/**
	 * 驳回任务
	 *
	 * @param rejectTaskForm Form
	 * @return ApiResult
	 */
	@PostMapping("/process/task/reject")
	public ApiResult<WfRunProcessVo> rejectTask(@RequestBody WfRejectTaskForm rejectTaskForm) {
		WfRunProcessVo procVo = activitiService.rejectTask(rejectTaskForm);
		return ApiResult.success(procVo);
	}

	/**
	 * 根据流程id终止流程（工作流）
	 *
	 * @param terminateTaskForm Form
	 * @return ApiResult
	 */
	@PostMapping("/process/terminate")
	public ApiResult terminateProcess(@RequestBody WfTerminateTaskForm terminateTaskForm) {
		activitiService.terminateProcess(terminateTaskForm);
		return ApiResult.success();
	}

	/**
	 * 根据流程id删除流程（工作流）
	 *
	 * @param procInstId 流程id
	 * @return ApiResult
	 */
	@DeleteMapping("/process/{procInstId}/delete")
	public ApiResult deleteProcess(@PathVariable("procInstId") String procInstId) {
		activitiService.deleteProcess(procInstId);
		return ApiResult.success();
	}

	/**
	 * 活动定义ID查询作务信息
	 *
	 * @param procInstId 流程id
	 * @param actDefId   活动ID
	 * @return WfTaskVo
	 */
	@GetMapping("/process/{procInstId}/{actDefId}/task/list")
	public ApiResult<List<WfTaskVo>> getTaskByActDefId(@PathVariable("procInstId") String procInstId, @PathVariable("actDefId") String actDefId) {
		List<WfTaskVo> tasks = activitiService.getTaskByActDefId(procInstId, actDefId);
		return ApiResult.success(tasks);
	}

	/**
	 * 根据流程id激活流程（工作流）
	 *
	 * @param procInstId 流程id
	 * @return ApiResult
	 */
	@DeleteMapping("/process/{procInstId}/active")
	public ApiResult activeProcess(@PathVariable("procInstId") String procInstId) {
		activitiService.activeProcess(procInstId);
		return ApiResult.success();
	}

	/**
	 * 根据流程id挂起流程（工作流）
	 *
	 * @param procInstId 流程id
	 * @return ApiResult
	 */
	@DeleteMapping("/process/{procInstId}/suspend")
	public ApiResult suspendProcess(@PathVariable("procInstId") String procInstId) {
		activitiService.suspendProcess(procInstId);
		return ApiResult.success();
	}

	/**
	 * 我的待办信息数量
	 * @param userId
	 * @return
	 */
	@GetMapping("/{userId}/unfinish/task/count")
	public ApiResult<Long> queryWfUnfinishCount(@PathVariable("userId") String userId) {
		Long count = activitiService.queryWfUnfinishCount(userId);
		return ApiResult.success(count);
	}

	/**
	 * 我的待办信息
	 * @param userId 用户
	 * @param pageSize 分类
	 * @param pageNum
	 * @param form
	 * @return
	 */
	@PostMapping("/unfinish/task/{userId}/{pageSize}/{pageNum}/list")
	public ApiResult<PageInfo<MyUnFinishTaskVo>> queryMyUnfinishTasks(@PathVariable("userId") String userId, @PathVariable("pageSize") int pageSize,
                                                                      @PathVariable("pageNum") int pageNum, @RequestBody WfUnFinishSearchForm form) {
		PageInfo<MyUnFinishTaskVo> taskList = activitiService.queryMyUnfinishTaskList(userId, pageSize, pageNum, form);
		return ApiResult.success(taskList);
	}

	/**
	 * 我的已办信息
	 *
	 * @param userId 用户
	 * @return ApiResult
	 */
	@PostMapping("/finish/task/{userId}/{pageSize}/{pageNum}/list")
	public ApiResult<PageInfo<MyFinishTaskVo>> queryMyFinishTasks(@PathVariable("userId") String userId, @PathVariable("pageSize") Integer pageSize,
                                                                  @PathVariable("pageNum") Integer pageNum, @RequestBody WfFinishSearchForm form) {
		PageInfo<MyFinishTaskVo> taskList = activitiService.queryMyFinishTaskList(userId, pageSize, pageNum, form);
		return ApiResult.success(taskList);
	}

	/**
	 * 我发起的
	 *
	 * @param userId 用户
	 * @return ApiResult
	 */
	@PostMapping("/mine/task/{userId}/{pageSize}/{pageNum}/list")
	public ApiResult<PageInfo<WfMineTaskVo>> queryMineTasks(@PathVariable("userId") String userId, @PathVariable("pageSize") int pageSize,
                                                            @PathVariable("pageNum") int pageNum, @RequestBody WfMineSearchForm form) {
		PageInfo<WfMineTaskVo> taskList = activitiService.queryWfMineTaskList(userId, pageSize, pageNum, form);
		return ApiResult.success(taskList);
	}

	/**
	 * 流程处理信息获取数据
	 *
	 * @param procInstIds 流程id
	 * @return WfProcLogVo
	 */
	@PostMapping(value = "/process/handle/log/list")
	public ApiResult queryProcessHandleLogList(@RequestBody List<String> procInstIds) throws ParseException {
		List<WfProcLogVo> wfProcLogVos = activitiService.queryProcessHandleLogList(procInstIds);
		return ApiResult.success(wfProcLogVos);
	}

	/**
	 * 流程进展日志
	 *
	 * @param procInstId 流程实例id
	 * @return WfLogDetailVo
	 */
	@PostMapping(value = "/process/handle/log/detail")
	public ApiResult queryProcessHandleLogDetailList(@RequestBody String procInstId) {
		WfLogDetailVo wfLogDetailVo = activitiService.queryProcessHandleLogDetail(procInstId);
		return ApiResult.success(wfLogDetailVo);
	}

	/**
	 * @param procInstId 流程实例id
	 * @return ApiResult
	 */
	@GetMapping(value = "/process/{procInstId}/taskId")
	public ApiResult queryTaskIdByProcInstrId(@PathVariable("procInstId") String procInstId) {
		String userId = request.getHeader("userId");
		return ApiResult.success(activitiService.queryTaskIdByUserIdAndProcInstrId(procInstId, userId));
	}

	/**
	 * 分配模版信息
	 *
	 * @param modelIds modelId
	 * @return ApiResult
	 */
	@PostMapping(value = "/process/assign/models")
	public ApiResult queryWfAssignList(@RequestBody List<String> modelIds) {
		List<ActModelVo> actModelVos = activitiService.queryWfAssignList(modelIds);
		return ApiResult.success(actModelVos);
	}

	/**
	 * 分配模版信息
	 *
	 * @param modelIds modelId
	 * @return ApiResult
	 */
	@PostMapping(value = "/process/assign/procdef")
	public ApiResult<WfProcessDefVo> queryProcDefByModulIdList(@RequestBody List<String> modelIds) {
		List<WfProcessDefVo> actModelVos = activitiService.queryProcDefByModulIdList(modelIds);
		return ApiResult.success(actModelVos);
	}

	/**
	 * 获取模版id
	 *
	 * @param actModelAddForm Form
	 * @return ApiResult
	 */
	@PostMapping(value = "/process/new/model")
	public ApiResult addActivityNewModel(@RequestBody ActModelAddForm actModelAddForm) {
		String modelId = activitiService.addModel(actModelAddForm);
		return ApiResult.success(modelId);
	}

	/**
	 * 发布模版
	 *
	 * @return ApiResult
	 */
	@PostMapping(value = "/process/release/{modelId}/model")
	public ApiResult releaseActivityNewModel(@PathVariable("modelId") String modelId) throws IOException {
		String retMsg = activitiService.deployProcess(modelId);
		return ApiResult.success(retMsg);
	}

	/**
	 * 删除模版
	 *
	 * @param modelId modelId
	 * @return ApiResult
	 */
	@PostMapping(value = "/process/delete/{modelId}/model")
	public ApiResult deleteActivityNewModel(@PathVariable("modelId") String modelId) {
		String retMsg = activitiService.deleteActivityNewModel(modelId);
		return ApiResult.success(retMsg);
	}

	/**
	 * 根据流程实例Id获取流程
	 *
	 * @param processInstanceId 流程实例id
	 * @return 流程实例
	 */
	@GetMapping(value = "/process/{processInstanceId}/processInstance")
	public ApiResult<WfProcessInstVo> getProcessInstanceById(@PathVariable("processInstanceId") String processInstanceId) {
		return ApiResult.success(this.activitiService.getProcessInstanceById(processInstanceId));
	}

	/**
	 * 根据流程实例Id获取流程活动实例
	 *
	 * @param activityInstanceId 活动实例id
	 * @return 流程活动实例
	 */
	@GetMapping(value = "/process/{activityInstanceId}/activityInstance")
	public ApiResult<WfActivityInstanceVo> getActivityInstanceByActivityInstanceId(@PathVariable("activityInstanceId") String activityInstanceId) {
		return ApiResult.success(this.activitiService.getActivityInstanceByActivityInstanceId(activityInstanceId));
	}

	/**
	 * 根据流程实例Id获取流程活动实例
	 *
	 * @param processInstanceId 流程实例id
	 * @return 流程活动实例
	 */
	@GetMapping(value = "/process/{processInstanceId}/activityInstance/list")
	public ApiResult<List<WfActivityInstanceVo>> getActivityInstanceByProcessInstanceId(@PathVariable("processInstanceId") String processInstanceId) {
		return ApiResult.success(this.activitiService.getActivityInstanceByProcessInstanceId(processInstanceId));
	}

	/**
	 * 根据工作项id获得工作项
	 *
	 * @param taskId 任务ID
	 * @return 任务
	 */
	@GetMapping(value = "/process/{taskId}/task")
	public ApiResult<WfTaskVo> getTaskByTaskId(@PathVariable("taskId") String taskId) {
		return ApiResult.success(this.activitiService.getTaskByTaskId(taskId));
	}

	/**
	 * 查询流程实例ID的所有工作项
	 *
	 * @param processInstanceId 流程ID
	 * @return 任务
	 */
	@GetMapping(value = "/process/{processInstanceId}/run/task/list")
	public ApiResult<List<WfTaskVo>> getRunTaskByProcessInstanceId(@PathVariable("processInstanceId") String processInstanceId) {
		return ApiResult.success(this.activitiService.getRunTaskByProcessInstanceId(processInstanceId));
	}

	/**
	 * 查询流程实例ID的所有工作项
	 *
	 * @param processInstanceId 流程ID
	 * @return 任务
	 */
	@GetMapping(value = "/process/{processInstanceId}/task/list")
	public ApiResult<List<WfTaskVo>> getTaskByProcessInstanceId(@PathVariable("processInstanceId") String processInstanceId) {
		return ApiResult.success(this.activitiService.getTaskByProcessInstanceId(processInstanceId));
	}

	/**
	 * 根据流程实例ID查询指定用户的工作项
	 *
	 * @param processInstanceId 流程ID
	 * @return 任务
	 * @oaran userId 用户ID
	 */
	@GetMapping(value = "/process/{processInstanceId}/user/{userId}/run/task/list")
	public ApiResult<List<WfRunTaskVo>> getRunTaskByProcessInstanceId(@PathVariable("processInstanceId") String processInstanceId, @PathVariable("userId") String userId) {
		return ApiResult.success(this.activitiService.getRunTaskByProcessInstanceId(processInstanceId, userId));
	}

	/**
	 * 得到任务的候选人
	 * @param taskId
	 * @return
	 */
	@GetMapping(value = "/process/{taskId}/candidate/list")
	public ApiResult<List<String>> getTaskCandidates(@PathVariable("taskId") String taskId){
		return ApiResult.success(this.activitiService.getTaskCandidates(taskId));
	}

}