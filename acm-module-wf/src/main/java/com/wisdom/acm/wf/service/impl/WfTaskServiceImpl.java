package com.wisdom.acm.wf.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.wisdom.acm.wf.enums.WfLogOptTypeEnum;
import com.wisdom.acm.wf.po.WfFormPo;
import com.wisdom.acm.wf.service.*;
import com.wisdom.acm.wf.vo.UnfinshTaskDetailVo;
import com.wisdom.acm.wf.vo.UnfinshTaskVo;
import com.wisdom.base.common.enums.ResultEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.CommActivitiService;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.form.WfClaimTaskForm;
import com.wisdom.base.common.form.WfFinishSearchForm;
import com.wisdom.base.common.form.WfMineSearchForm;
import com.wisdom.base.common.form.WfUnFinishSearchForm;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.util.*;
import com.wisdom.base.common.util.calc.calendar.Tools;
import com.wisdom.base.common.vo.UserVo;
import com.wisdom.base.common.vo.wf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WfTaskServiceImpl implements WfTaskService {

	@Autowired
	private CommActivitiService commActivitiService;

	@Autowired
	private CommUserService commUserService;

	@Autowired
	private WfFormService wfFormService;

	@Autowired
	private WfLogService wfLogService;

	@Autowired
	private WfProcessInsService wfProcessInsService;

	@Autowired
	protected org.dozer.Mapper dozerMapper;

	@Autowired
	private WfFormDataService wfFormDataService;

	/**
	 * 我的待只数量
	 * @param userId
	 * @return
	 */
	@Override
	public Long queryWfUnfinishCount(String userId){
		ApiResult<Long> result = commActivitiService.queryWfUnfinishCount(userId);
		if (result.getStatus() != ResultEnum.SECCESS.getCode()) {
			throw new BaseException(result.getMessage());
		}
		return result.getData();
	}

	@Override
	public UnfinshTaskVo queryWfUnfinshTaskMessage(String userId) throws ParseException {
		UnfinshTaskVo unfinshTaskVo = new UnfinshTaskVo();
		List<UnfinshTaskDetailVo> unfinshTaskDetailVos = new ArrayList<>();
		WfUnFinishSearchForm search = new WfUnFinishSearchForm(); //获取待办
		ApiResult<PageInfo<MyUnFinishTaskVo>> apiResult = commActivitiService.queryMyUnfinishTasks(userId,7, 1, search);
		PageInfo<MyUnFinishTaskVo> pageInfo = apiResult.getData();
		if(!ObjectUtils.isEmpty(pageInfo)) {
			List<MyUnFinishTaskVo> tasks = pageInfo.getList();
			unfinshTaskVo.setSize(ObjectUtils.isEmpty(tasks) ? 0 : tasks.size());
			if (!ObjectUtils.isEmpty(tasks)) {
				for (MyUnFinishTaskVo task : tasks) {
					UnfinshTaskDetailVo unfinshTaskDetailVo = new UnfinshTaskDetailVo();
					unfinshTaskDetailVo.setTaskName(task.getTaskName());
					unfinshTaskDetailVo.setCreatTime(task.getCreateTime());
					unfinshTaskDetailVo.setStayTime(DateUtil.getDatePoor(task.getCreateTime(), new Date()));
					unfinshTaskDetailVos.add(unfinshTaskDetailVo);
				}
			}
			unfinshTaskVo.setDetailVos(unfinshTaskDetailVos);
		}
		return unfinshTaskVo;
	}

	/**
	 * 我的待办
	 *
	 * @param pageSize
	 * @param pageNum
	 * @param userId
	 * @return
	 */
	@Override
	public PageInfo<MyUnFinishTaskVo> queryWfUnfinshTaskList(Integer pageSize, Integer pageNum, String userId, WfUnFinishSearchForm search) {
		ApiResult<PageInfo<MyUnFinishTaskVo>> result = commActivitiService.queryMyUnfinishTasks(userId, pageSize, pageNum, search); //获取待办
		if(!result.isSuccess()){
			throw new BaseException(result.getMessage());
		}
		PageInfo<MyUnFinishTaskVo> tasks = result.getData();
		Set<String> procInstIds = new HashSet<>();
		if (!ObjectUtils.isEmpty(tasks) && !ObjectUtils.isEmpty(tasks.getList())) {
			for (MyUnFinishTaskVo taskVo : tasks.getList()) {
				procInstIds.add(taskVo.getProcInstId());
			}
			List<MyUnFinishTaskVo> forms = wfFormService.queryTaskVoByProcInstIds(new ArrayList<>(procInstIds));
			if(!Tools.isEmpty(forms) && !ObjectUtils.isEmpty(tasks)){
				Map<String, MyUnFinishTaskVo> formMap = ListUtil.listToMap(forms, "procInstId", String.class);
				for (MyUnFinishTaskVo ufTaskVo : tasks.getList()) {
					MyUnFinishTaskVo form = formMap.get(ufTaskVo.getProcInstId());
					if(form != null){
						ufTaskVo.setProcInstName(form.getProcInstName());
						ufTaskVo.setStartUser(form.getStartUser());
						ufTaskVo.setStartUserOrg(form.getStartUserOrg());
						ufTaskVo.setBizType(form.getBizType());
						ufTaskVo.setRemark(form.getRemark());
					}
					ufTaskVo.setStayTime(DateUtil.getDatePoor(ufTaskVo.getCreateTime(), new Date()));
				}
			}
		}
		return tasks;
	}

	/**
	 * 我的已办
	 *
	 * @param pageSize
	 * @param pageNum
	 * @param userId
	 * @return
	 */
	@Override
	public PageInfo<MyFinishTaskVo> queryWfFinshTaskList(Integer pageSize, Integer pageNum, String userId, WfFinishSearchForm search) {
		ApiResult<PageInfo<MyFinishTaskVo>> result = commActivitiService.queryMyFinishTasks(userId, pageSize, pageNum, search); //获取已办
		if(!result.isSuccess()){
			throw new BaseException(result.getMessage());
		}
		PageInfo<MyFinishTaskVo> tasks = result.getData();
		Set<String> procInstIds = new HashSet<>();
		if (!ObjectUtils.isEmpty(tasks) && !ObjectUtils.isEmpty(tasks.getList())) {
			for (MyFinishTaskVo taskVo : tasks.getList()) {
				procInstIds.add(taskVo.getProcInstId());
			}
			List<MyUnFinishTaskVo> forms = wfFormService.queryTaskVoByProcInstIds(new ArrayList<>(procInstIds));
			if(!Tools.isEmpty(forms)){
				Map<String, MyUnFinishTaskVo> formMap = ListUtil.listToMap(forms, "procInstId", String.class);
				for (MyFinishTaskVo fTaskVo : tasks.getList()) {
					MyUnFinishTaskVo form = formMap.get(fTaskVo.getProcInstId());
					if(form != null){
						fTaskVo.setProcInstName(form.getProcInstName());
						fTaskVo.setRemark(form.getRemark());
						fTaskVo.setBizType(form.getBizType());
						fTaskVo.setStartUser(form.getStartUser());
						fTaskVo.setStartUserOrg(form.getStartUserOrg());
					}
					fTaskVo.setStayTime(DateUtil.getDatePoor(fTaskVo.getCreateTime(), fTaskVo.getEndTime()));
				}
			}
		}
		return tasks;
	}

	/**
	 * 我发起的
	 *
	 * @param pageSize
	 * @param pageNum
	 * @param userId
	 * @return
	 */
	@Override
	public PageInfo<WfMineTaskVo> queryWfMineTaskList(Integer pageSize, Integer pageNum, String userId, WfMineSearchForm search) {
		ApiResult<PageInfo<WfMineTaskVo>> result = commActivitiService.queryMineTasks(userId, pageSize, pageNum, search);
		if(!result.isSuccess()){
			throw new BaseException(result.getMessage());
		}
		PageInfo<WfMineTaskVo> mineTask = result.getData();
		if(!Tools.isEmpty(mineTask) && !Tools.isEmpty(mineTask.getList())) {
			Set<String> procInstIds = new HashSet<>();
			for (WfMineTaskVo taskVo : mineTask.getList()) {
				procInstIds.add(taskVo.getProcInstId());
			}
			List<MyUnFinishTaskVo> forms = wfFormService.queryTaskVoByProcInstIds(new ArrayList<>(procInstIds));
			if (!Tools.isEmpty(forms)) {
				Map<String, MyUnFinishTaskVo> formMap = ListUtil.listToMap(forms, "procInstId", String.class);
				for (WfMineTaskVo mTaskVo : mineTask.getList()) {
					MyUnFinishTaskVo form = formMap.get(mTaskVo.getProcInstId());
					if (form != null) {
						mTaskVo.setProcInstName(form.getProcInstName());
						mTaskVo.setRemark(form.getRemark());
						mTaskVo.setStartUser(form.getStartUser());
						mTaskVo.setStartUserOrg(form.getStartUserOrg());
						mTaskVo.setBizType(form.getBizType());
					}
				}
			}
		}
		return mineTask;
	}

	/**
	 * 根据工作项id获得工作项详情
	 * @param taskId　任务ID
	 * @param userId 用户Id
	 * @return 工作项详情
	 */
	@Override
	public WfTaskDetailVo getTaskByTaskId(String taskId, String userId){
		WfTaskDetailVo detailVo = null;
		WfTaskVo taskVo = this.wfProcessInsService.getTaskByTaskId(taskId);
		if(!Tools.isEmpty(taskVo)){
			detailVo =  this.dozerMapper.map(taskVo, WfTaskDetailVo.class);
			this.setForms(detailVo);
			this.setWfLog(detailVo);
			this.setClaimTask(detailVo, userId);
			this.setProcStatus(detailVo);
		}
		return detailVo;
	}

	/**
	 * 设置form
	 * @param detailVo 详请
	 */
	private void setForms(WfTaskDetailVo detailVo){
		List<MyUnFinishTaskVo> forms = this.wfFormService.queryTaskVoByProcInstIds(Lists.newArrayList(detailVo.getProcInstId()));
		if(!Tools.isEmpty(forms)){
			MyUnFinishTaskVo form = forms.get(0);
			detailVo.setDatas(this.wfFormDataService.queryFormDataVosByFormId(form.getId())); //得到业务数据);
			detailVo.setProcInstName(form.getProcInstName());
			detailVo.setStartUser(form.getStartUser());
			detailVo.setStartUserOrg(form.getStartUserOrg());
			detailVo.setBizType(form.getBizType());
			detailVo.setRemark(form.getRemark());
		}
	}

	/**
	 * 设置日志
	 * @param detailVo 工作项详情
	 */
	private void setWfLog(WfTaskDetailVo detailVo){
		WfLogDetailVo logVo = this.wfLogService.getLogVoListByProcInstId(detailVo.getProcInstId());
		if(!Tools.isEmpty(logVo)){
			List<WfProcLogDetailVo> wfLogs = new ArrayList<>();
			List<WfTaskVo> tasks = this.wfProcessInsService.getRunTaskByProcessInstanceId(detailVo.getProcInstId());
			if(!Tools.isEmpty(tasks)){
				Map<String, Object[]> candidate =  this.getTaskCandidates(tasks);
				for(WfTaskVo task: tasks){
					WfProcLogDetailVo logDetailVo = new WfProcLogDetailVo();
					logDetailVo.setCreateTime(task.getCreateTime());
					logDetailVo.setWorkItemName(task.getName());
					logDetailVo.setStayTime(DateUtil.getDatePoor(task.getCreateTime(), new Date()));
					logDetailVo.setOperateUser(FormatUtil.toString(candidate.get(task.getId())));
					wfLogs.add(logDetailVo);
				}
			}
			wfLogs.addAll(logVo.getWfLog());
			detailVo.setWfLog(wfLogs);
		}
	}

	/**
	 * 得到任务候选人
	 * @param tasks 任务
	 * @return 候选人
	 */
	private Map<String, Object[]> getTaskCandidates(List<WfTaskVo> tasks){
		Map<String, Object[]> taskCandidates = new HashMap<>();
		if(!Tools.isEmpty(tasks)){
			Set<Integer> userIds = new HashSet<>();
			for(WfTaskVo task: tasks) {
				ApiResult<List<String>> result = this.commActivitiService.getTaskCandidates(task.getId());
				if (!result.isSuccess()) {
					throw new BaseException(result.getMessage());
				}
				if(!Tools.isEmpty(result.getData())) {
					taskCandidates.put(task.getId(), result.getData().toArray());
					for (String userId : result.getData()){
						userIds.add(Tools.parseInt(userId));
					}
				}
			}
			Map<Integer, UserVo> userMap = this.getUserCandidates(new ArrayList<>(userIds));
			if(!Tools.isEmpty(userMap)){
				for(Object[] _userIds: taskCandidates.values()) {
					for (int i = 0; i < _userIds.length; i++){
						UserVo user = userMap.get(Tools.parseInt(_userIds[i]));
						_userIds[i] = Tools.isEmpty(user) ? "" : user.getName();
					}
				}
			}
		}
		return taskCandidates;
	}

	/**
	 * 查询用户
	 * @param userIds 用户ID
	 * @return
	 */
	private Map<Integer, UserVo> getUserCandidates(List<Integer> userIds){
		ApiResult<Map<Integer, UserVo>> result = this.commUserService.getUserVoMapByUsers(userIds);
		if (!result.isSuccess()) {
			throw new BaseException(result.getMessage());
		}
		return result.getData();
	}

	/**
	 * 设置权限
	 * @param detailVo 工作项详情
	 * @param userId  用户Id
	 */
	private void setClaimTask(WfTaskDetailVo detailVo, String userId){
		WfClaimTaskForm claim = new WfClaimTaskForm();
		claim.setUserId(userId);
		claim.setProcInstId(detailVo.getProcInstId());
		claim.setTaskId(detailVo.getId());
		ApiResult<WfClaimVo> result = this.commActivitiService.claimTask(claim); //调用工作流引擎发起工作流
		if (!result.isSuccess()) {
			throw new BaseException(result.getMessage());
		}
		WfClaimVo claimVo = result.getData();
		detailVo.setAgree(claimVo.isAgree());
		detailVo.setReject(claimVo.isReject());
		detailVo.setCancel(claimVo.isCancel());
		detailVo.setTerminate(claimVo.isTerminate());
	}

	/**
	 * 设置流程状态
	 * @param detailVo 工作项详情
	 */
	private void setProcStatus(WfTaskDetailVo detailVo){
		WfProcessInstVo procInstVo = this.wfProcessInsService.getProcessInstanceById(detailVo.getProcInstId());
		if(!Tools.isEmpty(procInstVo)){
			detailVo.setStatus(Tools.isEmpty(procInstVo.getEndTime()) ? "未完成" : "已完成");
		}
	}

	/**
	 * 根据工作项id获得工作项详情
	 * @param procInstId 工作项详情
	 * @param userId 用户Id
	 * @return
	 */
	@Override
	public WfTaskDetailVo getTaskByProcInstId(String procInstId, String userId){
		WfTaskDetailVo detailVo = null;
		WfProcessInstVo procInstVo = this.wfProcessInsService.getProcessInstanceById(procInstId);
		if(!Tools.isEmpty(procInstVo)){
			detailVo =  this.dozerMapper.map(procInstVo, WfTaskDetailVo.class);
			this.setForms(detailVo);
			this.setWfLog(detailVo);
			detailVo.setStatus(Tools.isEmpty(procInstVo.getEndTime()) ? "未完成" : "已完成");
		}
		return detailVo;
	}
}
