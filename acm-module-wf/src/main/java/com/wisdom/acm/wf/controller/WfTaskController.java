package com.wisdom.acm.wf.controller;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.wf.service.WfTaskService;
import com.wisdom.acm.wf.vo.UnfinshTaskVo;
import com.wisdom.base.common.form.WfFinishSearchForm;
import com.wisdom.base.common.form.WfMineSearchForm;
import com.wisdom.base.common.form.WfUnFinishSearchForm;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.msg.TableResultResponse;
import com.wisdom.base.common.util.BaseUtil;
import com.wisdom.base.common.util.calc.calendar.Tools;
import com.wisdom.base.common.vo.wf.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

/**
 * 任务
 */
@Api(tags = "流程待办服务")
@RestController
public class WfTaskController {

	@Autowired
	private WfTaskService taskService;

	@Autowired
	private HttpServletRequest request;

	@ApiOperation(value = "我的待办数量")
	@GetMapping("/unfinish/task/count")
	public ApiResult<Long> queryWfUnfinishCount() {
		String userId = Tools.toString(BaseUtil.getLoginUser().getId());
		Long count = this.taskService.queryWfUnfinishCount(userId);
		return ApiResult.success(count);
	}

	/**
	 * 我的待办条数（最多显示7条）
	 *
	 * @return
	 * @throws ParseException
	 */
	@PostMapping("/unfinish/task/message")
	public ApiResult<UnfinshTaskVo> queryWfUnfinishTaskMessage() throws ParseException {
		String userId = Tools.toString(BaseUtil.getLoginUser().getId());
		UnfinshTaskVo unfinshTaskVo = this.taskService.queryWfUnfinshTaskMessage(userId);
		return ApiResult.success(unfinshTaskVo);
	}

	@ApiOperation(value = "我的待办")
	@PostMapping("/unfinish/task/{pageSize}/{pageNum}/list")
	public ApiResult<List<MyUnFinishTaskVo>> queryWfUnfinishTask(@PathVariable("pageSize") @ApiParam(value = "每页数量", required = true) Integer pageSize,
																 @PathVariable("pageNum") @ApiParam(value = "第几页", required = true) Integer pageNum,
																 @RequestBody @ApiParam(value = "查询表单", required = false) WfUnFinishSearchForm search) {
		String userId = Tools.toString(BaseUtil.getLoginUser().getId());
		PageInfo<MyUnFinishTaskVo> pageInfo = this.taskService.queryWfUnfinshTaskList(pageSize, pageNum, userId, search);
		return new TableResultResponse(pageInfo);
	}

	@ApiOperation(value = "我的已办")
	@PostMapping("/finish/task/{pageSize}/{pageNum}/list")
	public ApiResult<List<MyFinishTaskVo>> queryWfFinishTask(
										@PathVariable("pageSize") @ApiParam(value = "每页数量", required = true) Integer pageSize,
									   	@PathVariable("pageNum") @ApiParam(value = "第几页", required = true) Integer pageNum,
										@RequestBody @ApiParam(value = "查询表单", required = false) WfFinishSearchForm search) {
		String userId = Tools.toString(BaseUtil.getLoginUser().getId());
		PageInfo<MyFinishTaskVo> pageInfo = this.taskService.queryWfFinshTaskList(pageSize, pageNum, userId, search);
		return new TableResultResponse(pageInfo);
	}

	@ApiOperation(value = "我发起的")
	@PostMapping("/mine/task/{pageSize}/{pageNum}/list")
	public ApiResult<List<WfMineTaskVo>> queryWfMineTask(
										@PathVariable("pageSize") @ApiParam(value = "每页数量", required = true) Integer pageSize,
									 	@PathVariable("pageNum") @ApiParam(value = "第几页", required = true) Integer pageNum,
										@RequestBody @ApiParam(value = "查询表单", required = false) WfMineSearchForm search) {
		String userId = Tools.toString(BaseUtil.getLoginUser().getId());
		PageInfo<WfMineTaskVo> pageInfo = this.taskService.queryWfMineTaskList(pageSize, pageNum, userId, search);
		return new TableResultResponse(pageInfo);
	}

	@ApiOperation(value = "根据工作项ID获得工作项详情")
	@GetMapping(value = "/task/{taskId}/info")
	public ApiResult<WfTaskDetailVo> getTaskByTaskId(
									@PathVariable("taskId") @ApiParam(value = "任务ID", required = true) String taskId) {
		String userId = Tools.toString(BaseUtil.getLoginUser().getId());
		WfTaskVo vo = this.taskService.getTaskByTaskId(taskId, userId);
		return ApiResult.success(vo);
	}

	@ApiOperation(value = "根据流程ID获得流程详情")
	@GetMapping(value = "/proc/{procInstId}/info")
	public ApiResult<WfTaskDetailVo> getTaskByProcInstId(
									@PathVariable("procInstId") @ApiParam(value = "流程实例ID", required = true) String procInstId) {
		String userId = Tools.toString(BaseUtil.getLoginUser().getId());
		WfTaskVo vo = this.taskService.getTaskByProcInstId(procInstId, userId);
		return ApiResult.success(vo);
	}
}
