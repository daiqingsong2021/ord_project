package com.wisdom.acm.wf.service.impl;

import com.wisdom.acm.wf.enums.WfLogOptTypeEnum;
import com.wisdom.acm.wf.form.WfLogAddForm;
import com.wisdom.acm.wf.po.WfBizTypePo;
import com.wisdom.acm.wf.po.WfFormDataPo;
import com.wisdom.acm.wf.po.WfFormPo;
import com.wisdom.acm.wf.service.*;
import com.wisdom.acm.wf.vo.WfDelegateVo;
import com.wisdom.acm.wf.vo.WfInstVo;
import com.wisdom.base.common.enums.ResultEnum;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.feign.CommActivitiService;
import com.wisdom.base.common.feign.CommUserService;
import com.wisdom.base.common.form.*;
import com.wisdom.base.common.msg.ApiResult;
import com.wisdom.base.common.util.BaseUtil;
import com.wisdom.base.common.util.FormatUtil;
import com.wisdom.base.common.util.ListUtil;
import com.wisdom.base.common.util.WebUtil;
import com.wisdom.base.common.util.calc.calendar.Tools;
import com.wisdom.base.common.vo.RoleUserVo;
import com.wisdom.base.common.vo.SysAllUserVo;
import com.wisdom.base.common.vo.UserVo;
import com.wisdom.base.common.vo.wf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@Service
public class WfProcessInsServiceImpl implements WfProcessInsService {

	@Autowired
	private WfBizTypeService wfBizTypeService;

	@Autowired(required = false)
	private CommActivitiService commActivitiService;

	@Autowired
	private WfFormService wfFormService;

	@Autowired
	private WfFormDataService wfFormDataService;

	@Autowired(required = false)
	private CommUserService commUserService;

	@Autowired
	private WfBizTypeService bizTypeService;

	@Autowired
	protected org.dozer.Mapper dozerMapper;

	@Autowired
	private WfLogService wfLogService;

	@Autowired
	private WfDelegateService wfDelegateService;

	/**
	 * 得到开始节点的后续活动候选人
	 *
	 * @param userId     用户
	 * @param bizType    业务代码
	 * @param procDefKey 流程定义
	 * @return 流程参与者
	 */
	@Override
	public WfCandidateVo getStartNextCandidate(String userId, String bizType, String procDefKey, WfStartProcessForm form) {
		Map<String, Object> vars = ObjectUtils.isEmpty(form.getVars()) ? new HashMap() : form.getVars();
		ApiResult<WfCandidateVo> result = this.commActivitiService.getStartNextCandidate(procDefKey, vars);
		if (result.getStatus() != ResultEnum.SECCESS.getCode()) {
			throw new BaseException(result.getMessage());
		}
		WfCandidateVo candidate = result.getData() != null ? result.getData() : new WfCandidateVo(); //得到系统定义参与者
		this.setCandidateGroup(candidate);
		this.setCandidateUser(candidate);
		this.setCustomStartNextCandidate(userId, bizType, procDefKey, candidate, form); //得到用户自定义参与者
		return candidate;
	}

	/**
	 * 得到开始节点的后续活动候选人
	 *
	 * @param userId     用户
	 * @param bizType    业务代码
	 * @param procDefKey 流程定义
	 * @return 流程参与者
	 */
	@Override
	public List<WfCandidateTreeVo> getStartNextCandidateTree(String userId, String bizType, String procDefKey, WfStartProcessForm form) {
		WfCandidateVo candidate = this.getStartNextCandidate(userId, bizType, procDefKey, form);
		return this.getCandidateTree(candidate);
	}

	/**
	 * 转换候选者
	 *
	 * @param candidate 候选者
	 * @return 候选者
	 */
	private List<WfCandidateTreeVo> getCandidateTree(WfCandidateVo candidate) {
		List<WfCandidateTreeVo> treeVos = new ArrayList<>();
		if (!Tools.isEmpty(candidate) && !Tools.isEmpty(candidate.getActivities())) {
			for (WfActivityVo activity : candidate.getActivities()) {
				WfCandidateTreeVo treeVo = new WfCandidateTreeVo(activity);
				treeVo.setActivityOnly(candidate.isActivityOnly());
				treeVo.setChildren(this.getCandidateGroupTree(activity.getCandidateGroups(), candidate.isActivityOnly()));
				treeVo.getChildren().addAll(this.getCandidateUserTree(activity.getCandidateUsers(), candidate.isActivityOnly()));
				treeVos.add(treeVo);
			}
		}
		return treeVos;
	}

	/**
	 * 从事件中获取参与者(调用发流流程调用自定义后续参与者)
	 *
	 * @param userId     用户ID
	 * @param procDefKey 流程Key
	 * @param candidate  后续参与者
	 * @return 参与者
	 */
	private void setCustomStartNextCandidate(String userId, String bizType, String procDefKey, WfCandidateVo candidate, WfStartProcessForm form) {
		WfRuningProcessForm wfForm = new WfRuningProcessForm(); //得到调用后台表单数据
		wfForm.setBizTypeCode(bizType);
		wfForm.setUserId(userId);
		wfForm.setProcDefKey(procDefKey);
		wfForm.setActivityName("发起流程");
		wfForm.setCandidate(candidate);
		wfForm.setVars(form.getVars());
		wfForm.setFormData(form.getFormData());
		WfBizTypePo bizTypePo = this.wfBizTypeService.queryWfTypeByTypeCode(bizType); //根据业务类型代码查询业务配置
		WfRunProcessVo procVo = this.callWorkFlowEvent(wfForm, bizTypePo.getEvent(), "customWorkFlowCandidate"); //调用工作流自定义参与者事件
		if (!ObjectUtils.isEmpty(procVo) && procVo.getCandidate() != null) {
			candidate.setActivities(procVo.getCandidate().getActivities());
		}
	}

	/**
	 * 设置候选组名称
	 *
	 * @param candidate 流程候选者
	 */
	private void setCandidateGroup(WfCandidateVo candidate) {
		List<Integer> ids = this.getGroupIds(candidate);
		if (!ObjectUtils.isEmpty(ids)) {
			ApiResult<Map<Integer, RoleUserVo>> result = this.commUserService.getRoleUserVoMapByIds(new ArrayList<>(ids));
			if (!result.isSuccess()) {
				throw new BaseException(result.getMessage());
			}
			this.setCandidateGroup(candidate, result.getData());
		}
	}

	/**
	 * 设置候选组名称
	 *
	 * @param candidate 流程候选者
	 * @param groupMap  组名称
	 */
	private void setCandidateGroup(WfCandidateVo candidate, Map<Integer, RoleUserVo> groupMap) {
		if (!Tools.isEmpty(candidate.getActivities()) && !Tools.isEmpty(candidate.getActivities()) && !Tools.isEmpty(groupMap)) {
			for (WfActivityVo activity : candidate.getActivities()) {
				if (!Tools.isEmpty(activity.getCandidateGroups())) {
					for (WfCandidateGroupVo group : activity.getCandidateGroups()) {
						this.setCandidateGroup(group, groupMap.get(Tools.parseInt(group.getId())));
					}
				}
			}
		}
	}

	/**
	 * 设置候选组名称
	 *
	 * @param group 流程候选组
	 * @param role  组
	 */
	private void setCandidateGroup(WfCandidateGroupVo group, RoleUserVo role) {
		if (!Tools.isEmpty(group) && !Tools.isEmpty(role) && !Tools.isEmpty(role.getRole())) {
			group.setCode(role.getRole().getCode());
			group.setName(role.getRole().getName());
			if (!Tools.isEmpty(role.getUser())) {
				List<WfCandidateUserVo> users = new ArrayList<>();
				for (UserVo uvo : role.getUser()) {
					users.add(new WfCandidateUserVo(uvo));
				}
				group.setCandidateUsers(users);
			}
		}
	}

	/**
	 * 获取用户组ID
	 *
	 * @param candidate 流程参与者
	 * @return 用户组ID
	 */
	private List<Integer> getGroupIds(WfCandidateVo candidate) {
		Set<Integer> ids = new HashSet<>();
		if (!Tools.isEmpty(candidate) && !Tools.isEmpty(candidate.getActivities())) {
			for (WfActivityVo activity : candidate.getActivities()) {
				if (!Tools.isEmpty(activity.getCandidateGroups())) {
					for (WfCandidateGroupVo group : activity.getCandidateGroups()) {
						ids.add(FormatUtil.parseInt(group.getId()));
					}
				}
			}
		}
		return new ArrayList<>(ids);
	}

	/**
	 * 设置用户名称
	 *
	 * @param candidate 流程参与者
	 * @return 流程参与者
	 */
	private void setCandidateUser(WfCandidateVo candidate) {
		List<Integer> ids = this.getUserIds(candidate);
		if (!ObjectUtils.isEmpty(ids)) {
			ApiResult<Map<Integer, UserVo>> result = this.commUserService.getUserVoMapByUsers(new ArrayList<>(ids));
			if (result.getStatus() != ResultEnum.SECCESS.getCode()) {
				throw new BaseException(result.getMessage());
			}
			this.setCandidateUser(candidate, result.getData());
		}
	}

	/**
	 * 设置用户名称
	 *
	 * @param candidate 流程候选者
	 * @param userMap   流程候选者
	 */
	private void setCandidateUser(WfCandidateVo candidate, Map<Integer, UserVo> userMap) {
		if (!Tools.isEmpty(candidate.getActivities()) && !Tools.isEmpty(userMap)) {
			for (WfActivityVo activity : candidate.getActivities()) {
				if (!Tools.isEmpty(activity.getCandidateUsers())) {
					for (WfCandidateUserVo user : activity.getCandidateUsers()) {
						UserVo userVo = userMap.get(Tools.parseInt(user.getId()));
						if (!Tools.isEmpty(userVo)) {
							user.setCode(userVo.getCode());
							user.setName(userVo.getName());
						}
					}
				}
			}
		}
	}

	/**
	 * 获取用户ID
	 *
	 * @param candidate 流程参与者
	 * @return 用户ID
	 */
	private List<Integer> getUserIds(WfCandidateVo candidate) {
		Set<Integer> ids = new HashSet<>();
		if (!Tools.isEmpty(candidate.getActivities())) {
			for (WfActivityVo activity : candidate.getActivities()) {
				if (!Tools.isEmpty(activity.getCandidateUsers())) {
					for (WfCandidateUserVo user : activity.getCandidateUsers()) {
						ids.add(Tools.parseInt(user.getId()));
					}
				}
			}
		}
		return new ArrayList<>(ids);
	}

	/**
	 * 得到下一个活动参与者
	 *
	 * @param userId 用户
	 * @param taskId 流程定义key
	 * @return 活动参与者
	 */
	@Override
	public WfCandidateVo getNextCandidate(String userId, String taskId) {
		ApiResult<WfCandidateVo> result = this.commActivitiService.getNextCandidate(taskId);
		if (!result.isSuccess()) {
			throw new BaseException(result.getMessage());
		}
		WfCandidateVo candidate = result.getData() != null ? result.getData() : new WfCandidateVo(); //得到系统定义参与者
		this.setCandidateGroup(candidate);
		this.setCandidateUser(candidate);
		this.setCustomNextCandidate(userId, taskId, candidate); //得到系统自定义参与者
		return candidate;
	}

	/**
	 * 得到下一个活动参与者
	 *
	 * @param userId 用户
	 * @param taskId 流程定义key
	 * @return 活动参与者
	 */
	@Override
	public List<WfCandidateTreeVo> getNextCandidateTree(String userId, String taskId) {
		WfCandidateVo candidate = this.getNextCandidate(userId, taskId); //得到系统定义参与者
		return this.getCandidateTree(candidate);
	}

	/**
	 * 从事件中获取参与者(调用下一个节点参与者)
	 *
	 * @param userId    用户ID
	 * @param taskId    任务ID
	 * @param candidate 后续参与者
	 * @return 参与者
	 */
	private void setCustomNextCandidate(String userId, String taskId, WfCandidateVo candidate) {
		WfTaskVo task = this.getTaskByTaskId(taskId);
		if (!ObjectUtils.isEmpty(task)) {
			WfFormPo wfForm = this.getFormByProcInstId(task.getProcInstId());
			if (!ObjectUtils.isEmpty(wfForm)) {
				WfBizTypePo bizTypePo = this.wfBizTypeService.queryWfTypeByTypeCode(wfForm.getTypeCode()); //根据业务类型代码查询业务配置
				List<WfFormDataVo> formDatas = this.getFormDataByFormId(wfForm.getId()); //得到业务数据
				if (!ObjectUtils.isEmpty(bizTypePo)) {
					WfRuningProcessForm runForm = new WfRuningProcessForm(); //得到调用后台表单数据
					runForm.setBizTypeCode(bizTypePo.getTypeCode());
					runForm.setUserId(userId);
					runForm.setProcDefId(task.getProcDefId());
					runForm.setProcInstId(task.getProcInstId());
					runForm.setActivityId(task.getActivityId());
					runForm.setActivityName(task.getName());
					runForm.setTaskId(task.getId());
					runForm.setCandidate(candidate);
					runForm.setFormData(formDatas);
					WfRunProcessVo procVo = this.callWorkFlowEvent(runForm, bizTypePo.getEvent(), "customWorkFlowCandidate"); //调用发起工作流后事件
					if (!ObjectUtils.isEmpty(procVo) && procVo.getCandidate() != null) {
						candidate.setActivities(procVo.getCandidate().getActivities());
					}
				}
			}
		}
	}

	/**
	 * 与者转成树形视图
	 *
	 * @param groups 参与者树形
	 * @param groups parentId
	 * @return 活动参与者
	 */
	private List<WfCandidateTreeVo> getCandidateGroupTree(List<WfCandidateGroupVo> groups, boolean activityOnly) {
		List<WfCandidateTreeVo> treeVos = new ArrayList<>();
		int uuid = 0;
		if (!Tools.isEmpty(groups)) {
			for (WfCandidateGroupVo group : groups) {
				WfCandidateTreeVo treeVo = new WfCandidateTreeVo(group);
				treeVo.setActivityOnly(activityOnly);
				treeVo.setChildren(this.getCandidateUserTree(group.getCandidateUsers(), activityOnly));
				treeVos.add(treeVo);
			}
		}
		return treeVos;
	}

	/**
	 * 与者转成树形视图
	 *
	 * @param users 参与者树形
	 * @return 活动参与者
	 */
	private List<WfCandidateTreeVo> getCandidateUserTree(List<WfCandidateUserVo> users, boolean activityOnly) {
		List<WfCandidateTreeVo> treeVos = new ArrayList<>();
		if (!Tools.isEmpty(users)) {
			int uuid = 0;
			for (WfCandidateUserVo user : users) {
				WfCandidateTreeVo treeVo = new WfCandidateTreeVo(user);
				treeVo.setActivityOnly(activityOnly);
				treeVos.add(treeVo);
			}
		}
		return treeVos;
	}

	/**
	 * 得到当前任务能驳回的任务
	 *
	 * @param taskId 任务ID
	 * @return 活动节点ID
	 */
	@Override
	public List<WfActivityVo> getRejectActivity(String taskId) {
		ApiResult<List<WfActivityVo>> result = this.commActivitiService.getRejectActivity(taskId);
		if (result.getStatus() != ResultEnum.SECCESS.getCode()) {
			throw new BaseException(result.getMessage());
		}
		return result.getData();
	}

	/**
	 * 发起工作流
	 *
	 * @param form Form
	 * @return 流程实例
	 */
	@Override
	public WfRunProcessVo startProcess(WfStartProcessForm form) {
		WfRunProcessVo procVo = new WfRunProcessVo();
		WfBizTypePo bizType = this.wfBizTypeService.queryWfTypeByTypeCode(form.getBizTypeCode()); //根据业务类型代码查询业务配置
		if (!ObjectUtils.isEmpty(bizType)) {
			this.addPartDelegate(form.getBizTypeCode(), form.getNextActPart());
			ApiResult<WfRunProcessVo> result = this.commActivitiService.startProcess(form); //调用工作流引擎发起工作流
			if (result.getStatus() != ResultEnum.SECCESS.getCode()) {
				throw new BaseException(result.getMessage());
			}
			procVo = result.getData();
			this.saveWfForm(form, procVo.getProcInstId()); //保存业务数据
			WfRuningProcessForm wfForm = this.getWfRunProcForm(form, procVo); //得到调用后台表单数据
			this.callWorkFlowEvent(wfForm, bizType.getEvent(), "startWorkFlowAfter"); //调用发起工作流后事件
			this.startProcessLog(form, procVo);
		}
		return procVo;
	}

	/**
	 * 增加代理人
	 * @param bizTypeCode 业务代码
	 * @param nextActPart 下一节点参与者
	 */
	private void addPartDelegate(String bizTypeCode, List<WfActivityVo> nextActPart){
		if(!Tools.isEmpty(nextActPart)){
			List<Integer> users = new ArrayList<>();
			for (WfActivityVo act : nextActPart){
				if(!Tools.isEmpty(act.getCandidateUsers())){
					for (WfCandidateUserVo userVo : act.getCandidateUsers()){
						users.add(Tools.parseInt(userVo.getId()));
					}
				}
			}
			if(!Tools.isEmpty(users)){
				List<WfDelegateVo> delegateVos = this.wfDelegateService.selectWfDelegateVosByBizTypeCode(bizTypeCode, users);
				this.addPartDelegate(nextActPart, delegateVos);
			}
		}
	}

	/**
	 * 增加代理人
	 * @param nextActPart
	 * @param delegateVos
	 */
	private void addPartDelegate(List<WfActivityVo> nextActPart, List<WfDelegateVo> delegateVos){
		if(!Tools.isEmpty(delegateVos)){
			for (WfActivityVo act : nextActPart){
				if(!Tools.isEmpty(act.getCandidateUsers())){
					List<WfCandidateUserVo> candidates = new ArrayList<>(act.getCandidateUsers());
					for (WfCandidateUserVo candidate : act.getCandidateUsers()){
						List<WfDelegateVo> delegates = this.getPartDelegates(candidate.getId(), delegateVos); //得到候选的人代理人
						if(!Tools.isEmpty(delegates)){
							for(WfDelegateVo delegate : delegates) {
								boolean isExists = this.isExistsCandidateUser(delegate, candidates); //判断代理人是否在候选人中
								if (!isExists) {
									candidates.add(this.getCandidateUser(candidate, delegate));
								}
							}
						}
					}
					act.setCandidateUsers(candidates);
				}
			}
		}
	}

	/**
	 * 查找候选人的代理人
	 * @param candidateUser 候选人
	 * @param delegateVos 代理人
	 * @return 代理人
	 */
	private List<WfDelegateVo> getPartDelegates(String candidateUser, List<WfDelegateVo> delegateVos){
		List<WfDelegateVo> delegates = new ArrayList<>();
		for (WfDelegateVo delegate : delegateVos){
			if(delegate.getAssignee() != null &&  delegate.getAttorney() != null //
				&& Tools.parseInt(candidateUser) == Tools.parseInt(delegate.getAssignee().getId()) ){
				delegates.add(delegate);
			}
		}
		return delegates;
	}

	/**
	 * 判断候选人是否在候选人中
	 * @param delegate 代理人
	 * @param candidateUsers 候选人
	 * @return 是否在候选人中
	 */
	private boolean isExistsCandidateUser(WfDelegateVo delegate, List<WfCandidateUserVo> candidateUsers){
		for (WfCandidateUserVo candidate : candidateUsers) {
			if (Tools.parseInt(candidate.getId()) == Tools.parseInt(delegate.getAttorney())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 代理人转候选人
	 * @param assignor 委托人
	 * @param delegate 代理人
	 * @return 候选人
	 */
	private WfCandidateUserVo getCandidateUser(WfCandidateUserVo assignor, WfDelegateVo delegate){
		WfCandidateUserVo candidate = new WfCandidateUserVo();
		candidate.setId(Tools.toString(delegate.getAttorney().getId()));
		candidate.setCode(delegate.getAttorney().getCode());
		candidate.setName(delegate.getAttorney().getName());
		candidate.setAssignor(new UserVo(Tools.parseInt(assignor.getId()), assignor.getCode(), assignor.getName()));
		return candidate;
	}

	/**
	 * 发起流程运行
	 *
	 * @param from 开始form
	 * @param procVo 流程实例
	 * @return 流流运行form
	 */
	private WfRuningProcessForm getWfRunProcForm(WfStartProcessForm from, WfRunProcessVo procVo) {
		WfRuningProcessForm runFrom = new WfRuningProcessForm();
		runFrom.setUserId(from.getUserId());
		runFrom.setBizTypeCode(from.getBizTypeCode());
		runFrom.setProcDefKey(from.getProcDefKey());
		runFrom.setProcDefId(procVo.getProcDefId());
		runFrom.setProcInstId(procVo.getProcInstId()); //流程实例ID
		runFrom.setActivityId(procVo.getActivityId()); //活动ID
		runFrom.setActivityName(procVo.getActivityName()); //活动名称
		runFrom.setTaskId(procVo.getTaskId()); //任务ID
		runFrom.setVars(from.getVars());
		runFrom.setFormData(from.getFormData());
		return runFrom;
	}

	/**
	 * 发起流程日志
	 *
	 * @param form   表单
	 * @param procVo 流程运行VO
	 */
	private void startProcessLog(WfStartProcessForm form, WfRunProcessVo procVo) {
		WfLogAddForm logAddForm = new WfLogAddForm();
		logAddForm.setOptType(WfLogOptTypeEnum.START.getCode());
		logAddForm.setContent(form.getComment());
		logAddForm.setUser(BaseUtil.getLoginUser().getName());
		logAddForm.setProcInstId(procVo.getProcInstId());
		logAddForm.setActivityId(procVo.getActivityId());
		logAddForm.setActivity(procVo.getActivityName());
		logAddForm.setStartTime(new Date());
		logAddForm.setStayTime(new Date());
		this.setProcessNextActPartLog(logAddForm, form.getNextActPart());
		wfLogService.addWfLog(logAddForm);
	}

	/**
	 * 设置后参活动节点信息
	 *
	 * @param form     日志
	 * @param partList 参与者
	 */
	private void setProcessNextActPartLog(WfLogAddForm form, List<WfActivityVo> partList) {
		List<String> userNames = new ArrayList<>(); //待处理人;
		if (!Tools.isEmpty(partList)) {
			for (WfActivityVo activity : partList) {
				if (!Tools.isEmpty(activity.getCandidateGroups())) {
					for (WfCandidateGroupVo group : activity.getCandidateGroups()) {
						userNames.add(group.getName());
					}
				}
				if (!Tools.isEmpty(activity.getCandidateUsers())) {
					for (WfCandidateUserVo user : activity.getCandidateUsers()) {
						userNames.add(user.getName() + (!Tools.isEmpty(user.getAssignor()) ? "(代理)" : ""));
					}
				}
			}
		}
		form.setHandleUser(FormatUtil.toString(userNames));
	}

	/**
	 * 认领任务
	 *
	 * @param claimTaskForm Form
	 * @return 认领VO
	 */
	@Override
	public WfClaimVo claimTask(WfClaimTaskForm claimTaskForm) {
		WfClaimVo vo = new WfClaimVo();
		WfFormPo form = this.getFormByProcInstId(claimTaskForm.getProcInstId());
		if (!ObjectUtils.isEmpty(form)) {
			WfBizTypePo bizType = this.wfBizTypeService.queryWfTypeByTypeCode(form.getTypeCode()); //根据业务类型代码查询业务配置
			ApiResult<WfClaimVo> result = this.commActivitiService.claimTask(claimTaskForm); //调用工作流引擎发起工作流
			if (!result.isSuccess()) {
				throw new BaseException(result.getMessage());
			}
			vo = result.getData();
			vo.setFormId(form.getId());
			vo.setBizTypeCode(form.getTypeCode());
			if (!ObjectUtils.isEmpty(form)) {
				vo.setUrl(bizType.getUrl());
			}
		}
		return vo;
	}

	/**
	 * 保存业务数据
	 *
	 * @param startForm  开始from
	 * @param procInstId 流程实例ID
	 */
	private void saveWfForm(WfStartProcessForm startForm, String procInstId) {
		WfFormPo wfFormPo = new WfFormPo();
		wfFormPo.setProcInstId(procInstId);
		wfFormPo.setTypeCode(startForm.getBizTypeCode());
		wfFormPo.setTitle(startForm.getTitle());
		wfFormPo.setRemark(startForm.getRemark());
		this.wfFormService.insert(wfFormPo);
		this.saveWfFormData(wfFormPo, startForm.getFormData());
	}

	/**
	 * 保存业务数据
	 *
	 * @param wfFormPo 流程业务表单
	 * @param fromData 流程业务数据
	 */
	private void saveWfFormData(WfFormPo wfFormPo, List<WfFormDataVo> fromData) {
		for (WfFormDataVo vo : fromData) {
			WfFormDataPo dataPo = new WfFormDataPo();
			dataPo.setFormId(wfFormPo.getId());
			dataPo.setBizType(vo.getBizType());
			dataPo.setBizId(vo.getBizId());
			this.wfFormDataService.insert(dataPo);
		}
	}



	/**
	 * 通过流程事件查找URL
	 *
	 * @param eventType 事件类型
	 * @return URL
	 */
	private String getEventUrl(String eventType) {
		if ("customWorkFlowCandidate".equals(eventType)) {
			return "/wf/custom/workflow/candidate"; //事件
		} else if ("startWorkFlowAfter".equals(eventType)) {
			return "/wf/start/workflow/after"; //发起流程后事件
		} else if ("completeWorkFlowAfter".equals(eventType)) {
			return "/wf/complete/workflow/after"; //完成流程事件后
		} else if ("completeWorkFlowBefore".equals(eventType)) {
			return "/wf/complete/workflow/before"; //完成流程事件前
		} else if ("terminateWorkFlowAfter".equals(eventType)) {
			return "/wf/terminate/workflow/after"; //终止流程事件后
		} else if ("terminateWorkFlowBefore".equals(eventType)) {
			return "/wf/terminate/workflow/before"; //终止流程事件前
		} else if ("deleteWorkFlowAfter".equals(eventType)) {
			return "/wf/delete/workflow/before"; //删除流程事件后
		} else if ("deleteWorkFlowBefore".equals(eventType)) {
			return "/wf/delete/workflow/before"; //删除流程事件前
		} else if ("executeTaskAfter".equals(eventType)) {
			return "/wf/execute/task/after"; //执行工作项事件后
		} else if ("executeTaskBefore".equals(eventType)) {
			return "/wf/execute/task/before"; //执行工作项事件前
		} else if ("backActivityAfter".equals(eventType)) {
			return "/wf/back/activity/after"; //驳回活动事件后
		} else if ("backActivityBefore".equals(eventType)) {
			return "/wf/back/activity/before"; //回活动事件前
		}
		throw new BaseException("eventType not find!");
	}

	/**
	 * 调用发起工作流后事件
	 *
	 * @param form      表单
	 * @param url       url
	 * @param eventType 事件类型
	 * @return 工作流运行VO
	 * HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	 * String ip = ClientUtil.getClientIp(request);
	 * int port = request.getServerPort();
	 * ApiResult apiResult = restTemplate.postForObject(url, entity, ApiResult.class);
	 * WfRuningProcessVo procVo = this.dozerMapper.map(apiResult.getData(), WfRuningProcessVo.class);
	 */
	private WfRunProcessVo callWorkFlowEvent(WfRuningProcessForm form, String url, String eventType) {
		url = url + this.getEventUrl(eventType);
		/*HttpHeaders headers = WebUtil.getHttpHeaders();
		HttpEntity<WfRuningProcessForm> entity = new HttpEntity<>(form, headers);
		RestTemplate restTemplate = new RestTemplate();
		ApiResult<?> apiResult = restTemplate.exchange(url, HttpMethod.POST, entity,
			new ParameterizedTypeReference<ApiResult<?>>() {}).getBody();
		if (!apiResult.isSuccess()) {
			throw new BaseException(apiResult.getMessage());
		} else {
			return this.dozerMapper.map(apiResult.getData(), WfRunProcessVo.class);
		}*/
		return WebUtil.post(url, form, WfRunProcessVo.class);
	}

	/**
	 * 完成任务
	 *
	 * @param form 表单
	 * @param task 任务
	 * @return 工作流运行VO
	 */
	@Override
	public WfRunProcessVo completeTask(WfCompleteTaskForm form, WfTaskVo task) {
		WfRunProcessVo procVo = null;
		WfFormPo wfForm = this.getFormByProcInstId(form.getProcInstId());
		if (!ObjectUtils.isEmpty(wfForm)) {
			List<WfFormDataVo> formDatas = this.getFormDataByFormId(wfForm.getId()); //得到业务数据
			if (!ObjectUtils.isEmpty(formDatas)) {
				WfBizTypePo bizType = this.wfBizTypeService.queryWfTypeByTypeCode(wfForm.getTypeCode()); //根据业务类型代码查询业务配置
				if (!ObjectUtils.isEmpty(bizType)) {
					this.addPartDelegate(wfForm.getTypeCode(), form.getNextActPart()); //增加代理人
					WfRuningProcessForm procFrom = this.getWfRunProcForm(form.getUserId(), bizType.getTypeCode(), formDatas, task);
					procVo = this.callWorkFlowEvent(procFrom, bizType.getEvent(), "executeTaskBefore"); //调用任务前事件
					form.setVars(!ObjectUtils.isEmpty(procVo) && !ObjectUtils.isEmpty(procVo.getVars()) ? procVo.getVars() : null); //变量
					procVo = this.completeTask(form); //完成任务
					if (procVo.isProcComplete()) {
						this.callWorkFlowEvent(procFrom, bizType.getEvent(), "completeWorkFlowAfter"); //调用流程完成后事件
					} else {
						this.callWorkFlowEvent(procFrom, bizType.getEvent(), "executeTaskAfter"); //调用任务后事件
					}
					this.completeTaskLog(form, procVo, task); //写审批日志
				}
			}
		}
		return procVo;
	}

	/**
	 * 完成任务
	 *
	 * @param form 表单
	 * @return 流程运行VO
	 */
	private WfRunProcessVo completeTask(WfCompleteTaskForm form) {
		ApiResult<WfRunProcessVo> result = this.commActivitiService.completeTask(form); //完成任务
		if (!result.isSuccess()) {
			throw new BaseException(result.getMessage());
		} else {
			return result.getData();
		}
	}

	/**
	 * 得到流程运行表单
	 *
	 * @param userId    完成表单
	 * @param bizType   业务对象
	 * @param formDatas 业务数据
	 * @param task      任务
	 * @return WfRuningProcessForm
	 */
	private WfRuningProcessForm getWfRunProcForm(String userId, String bizType, List<WfFormDataVo> formDatas, WfTaskVo task) {
		WfRuningProcessForm runFrom = new WfRuningProcessForm();
		runFrom.setFormData(formDatas);
		runFrom.setUserId(userId);
		runFrom.setBizTypeCode(bizType);
		runFrom.setProcInstId(task.getProcInstId()); //流程实例ID
		runFrom.setActivityId(task.getActivityId()); //任务定义ID
		runFrom.setActivityName(task.getName()); //任务名称
		runFrom.setTaskId(task.getId()); //任务ID
		return runFrom;
	}

	/**
	 * 完成任务日志
	 *
	 * @param form   表单
	 * @param procVo 流程运行VO
	 */
	private void completeTaskLog(WfCompleteTaskForm form, WfRunProcessVo procVo, WfTaskVo task) {
		WfLogAddForm logAddForm = new WfLogAddForm();
		logAddForm.setOptType(procVo.isProcComplete() ? WfLogOptTypeEnum.APPROVE.getCode() : WfLogOptTypeEnum.SUBMIT.getCode());
		logAddForm.setContent(form.getComment());
		logAddForm.setUser(BaseUtil.getLoginUser().getName());
		logAddForm.setProcInstId(procVo.getProcInstId());
		logAddForm.setActivityId(procVo.getActivityId());
		logAddForm.setActivity(procVo.getActivityName());
		logAddForm.setStartTime(task.getCreateTime());
		logAddForm.setStayTime(new Date());
		this.setProcessNextActPartLog(logAddForm, form.getNextActPart());
		this.wfLogService.addWfLog(logAddForm);
	}

	/**
	 * 撤销任务
	 *
	 * @param form 表单
	 * @param task 任务
	 * @return 流程运行VO
	 */
	@Override
	public WfRunProcessVo cancelTask(WfRejectTaskForm form, WfTaskVo task) {
		WfRunProcessVo procVo = null;
		WfFormPo wfForm = this.getFormByProcInstId(form.getProcInstId());
		if (!ObjectUtils.isEmpty(form)) {
			List<WfFormDataVo> formDatas = this.getFormDataByFormId(wfForm.getId());
			if (!ObjectUtils.isEmpty(formDatas)) {
				WfBizTypePo wfBizTypePo = this.wfBizTypeService.queryWfTypeByTypeCode(wfForm.getTypeCode()); //根据业务类型代码查询业务配置
				if (!ObjectUtils.isEmpty(wfBizTypePo)) {
					//WfRuningProcessForm procForm = this.getWfRunProcForm(form.getUserId(), wfForm.getTypeCode(), formDatas, task);
					//this.callWorkFlowEvent(procForm, wfBizTypePo.getEvent(), "backActivityBefore"); //调用任务前事件
					procVo = this.cancelTask(form);
					//this.callWorkFlowEvent(procForm, wfBizTypePo.getEvent(), "backActivityAfter"); //调用任务后事件
				}
				this.cancelTaskLog(form, procVo, task); //写流程日志
			}
		}
		return procVo;
	}

	/**
	 * 任务撤销
	 *
	 * @param form 表单
	 * @return 流程运行VO
	 */
	private WfRunProcessVo cancelTask(WfRejectTaskForm form) {
		ApiResult<WfRunProcessVo> result = this.commActivitiService.cancelTask(form); //驳回任务
		if (result.getStatus() != ResultEnum.SECCESS.getCode()) {
			throw new BaseException(result.getMessage());
		} else {
			return result.getData();
		}
	}

	/**
	 * 撤销日志
	 *
	 * @param form 表单
	 */
	private void cancelTaskLog(WfRejectTaskForm form, WfRunProcessVo procVo, WfTaskVo task) {
		WfLogAddForm logAddForm = new WfLogAddForm();
		logAddForm.setOptType(WfLogOptTypeEnum.CANCEL.getCode());
		logAddForm.setContent(form.getComment());
		logAddForm.setUser(BaseUtil.getLoginUser().getName());
		logAddForm.setProcInstId(form.getProcInstId());
		logAddForm.setActivityId(FormatUtil.toString(task.getActivityId()));
		logAddForm.setActivity(task.getName());
		logAddForm.setStartTime(task.getCreateTime());
		logAddForm.setStayTime(new Date());
		//logAddForm.setHandleUser(FormatUtil.toString(this.getUserNameById(FormatUtil.parseInt(procVo.getUserId()))));
		this.wfLogService.addWfLog(logAddForm);
	}

	/**
	 * 回退任务
	 *
	 * @param form 表单
	 * @param task 任务
	 * @return 流程运行VO
	 */
	@Override
	public WfRunProcessVo rejectTask(WfRejectTaskForm form, WfTaskVo task) {
		WfRunProcessVo procVo = null;
		WfFormPo wfForm = this.getFormByProcInstId(form.getProcInstId());
		if (!ObjectUtils.isEmpty(form)) {
			List<WfFormDataVo> formDatas = this.getFormDataByFormId(wfForm.getId());
			if (!ObjectUtils.isEmpty(formDatas)) {
				WfBizTypePo wfBizTypePo = this.wfBizTypeService.queryWfTypeByTypeCode(wfForm.getTypeCode()); //根据业务类型代码查询业务配置
				if (!ObjectUtils.isEmpty(wfBizTypePo)) {
					WfRuningProcessForm procForm = this.getWfRunProcForm(form.getUserId(), wfForm.getTypeCode(), formDatas, task);
					this.callWorkFlowEvent(procForm, wfBizTypePo.getEvent(), "backActivityBefore"); //调用任务前事件
					procVo = this.rejectTask(form);
					this.callWorkFlowEvent(procForm, wfBizTypePo.getEvent(), "backActivityAfter"); //调用任务后事件
				}
				this.rejectTaskLog(form, procVo, task); //写流程日志
			}
		}
		return this.getWfRuningProcessVo(form, task);
	}

	/**
	 * 任务驳回
	 *
	 * @param form 表单
	 * @return 流程运行VO
	 */
	private WfRunProcessVo rejectTask(WfRejectTaskForm form) {
		ApiResult<WfRunProcessVo> result = this.commActivitiService.rejectTask(form); //驳回任务
		if (result.getStatus() != ResultEnum.SECCESS.getCode()) {
			throw new BaseException(result.getMessage());
		} else {
			return result.getData();
		}
	}

	/**
	 * 驳回日志
	 *
	 * @param form 表单
	 */
	private void rejectTaskLog(WfRejectTaskForm form, WfRunProcessVo procVo, WfTaskVo task) {
		WfLogAddForm logAddForm = new WfLogAddForm();
		logAddForm.setOptType(WfLogOptTypeEnum.REJECT.getCode());
		logAddForm.setContent(form.getComment());
		logAddForm.setUser(BaseUtil.getLoginUser().getName());
		logAddForm.setProcInstId(form.getProcInstId());
		logAddForm.setActivityId(FormatUtil.toString(task.getActivityId()));
		logAddForm.setActivity(task.getName());
		logAddForm.setStartTime(task.getCreateTime());
		logAddForm.setStayTime(new Date());
		logAddForm.setHandleUser(FormatUtil.toString(this.getUserNameById(FormatUtil.parseInt(procVo.getUserId()))));
		this.wfLogService.addWfLog(logAddForm);
	}

	/**
	 * 根据用户ID查询用户名称
	 *
	 * @param userId 用户ID
	 * @return 用户名称
	 */
	private String getUserNameById(Integer userId) {
		UserVo user = this.commUserService.getUserVoByUserId(userId);
		if (!Tools.isEmpty(user)) {
			return user.getName();
		}
		return null;
	}

	/**
	 * 得到流程运行VO
	 *
	 * @param form 表单
	 * @param task 任务
	 * @return 流程运行VO
	 */
	private WfRunProcessVo getWfRuningProcessVo(WfRejectTaskForm form, WfTaskVo task) {
		WfRunProcessVo procVo = new WfRunProcessVo();
		procVo.setProcDefId(task.getProcDefId());
		procVo.setProcInstId(task.getProcInstId());
		procVo.setActivityId(task.getActivityId());
		procVo.setActivityName(task.getName());
		procVo.setTaskId(task.getId());
		procVo.setUserId(form.getUserId());
		return procVo;
	}

	/**
	 * 根据流程实例ID查找表单ID
	 *
	 * @param procInstId 流程实例ID
	 * @return WfFormPo
	 */
	private WfFormPo getFormByProcInstId(String procInstId) {
		if(Tools.isEmpty(procInstId)) {
			return null;
		}
		Example formExample = new Example(WfFormPo.class);
		Example.Criteria formCriteria = formExample.createCriteria();
		formCriteria.andEqualTo("procInstId", procInstId);
		return this.wfFormService.selectOneByExample(formExample);
	}

	/**
	 * 根据流程表单ID查询审批数据
	 *
	 * @param formId 表单ID
	 * @return WfFormDataVo
	 */
	private List<WfFormDataVo> getFormDataByFormId(Integer formId) {
		List<WfFormDataVo> vos = new ArrayList<>();
		if(!Tools.isEmpty(formId)) {
			Example dataExample = new Example(WfFormDataPo.class);
			Example.Criteria dataCriteria = dataExample.createCriteria();
			dataCriteria.andEqualTo("formId", formId);
			List<WfFormDataPo> formDatas = this.wfFormDataService.selectByExample(dataExample);
			if (!ObjectUtils.isEmpty(formDatas)) {
				for (WfFormDataPo po : formDatas) {
					WfFormDataVo vo = new WfFormDataVo();
					vo.setFormId(vo.getFormId());
					vo.setBizId(po.getBizId());
					vo.setBizType(po.getBizType());
					vo.setPassed(po.getPassed());
					vos.add(vo);
				}
			}
		}
		return vos;
	}

	/**
	 * 终止流程实例
	 *
	 * @param form 表单
	 */
	@Override
	public void terminateProcess(WfTerminateTaskForm form, WfTaskVo task) {
		WfFormPo wfForm = this.getFormByProcInstId(form.getProcInstId());
		if (!ObjectUtils.isEmpty(form)) {
			List<WfFormDataVo> formDatas = this.getFormDataByFormId(wfForm.getId());
			if (!ObjectUtils.isEmpty(formDatas)) {
				WfBizTypePo wfBizTypePo = this.wfBizTypeService.queryWfTypeByTypeCode(wfForm.getTypeCode()); //根据业务类型代码查询业务配置
				if (!ObjectUtils.isEmpty(wfBizTypePo)) {
					WfRuningProcessForm procForm = this.getWfRunProcForm(form, wfForm.getTypeCode(), formDatas, task);
					this.callWorkFlowEvent(procForm, wfBizTypePo.getEvent(), "terminateWorkFlowBefore"); //调用任务前事件
					ApiResult result = this.commActivitiService.terminateProcess(form); //调用工作流引擎发起工作流
					if (result.getStatus() != ResultEnum.SECCESS.getCode()) {
						throw new BaseException(result.getMessage());
					}
					this.callWorkFlowEvent(procForm, wfBizTypePo.getEvent(), "terminateWorkFlowAfter"); //调用任务前事件
				}
			}
			this.terminateProcessLog(form, task);
		}
	}

	/**
	 * 得到流程运行表单
	 *
	 * @param form      表单
	 * @param bizType   业务类型
	 * @param formDatas 业务ID
	 * @return 流程运行表单
	 */
	private WfRuningProcessForm getWfRunProcForm(WfTerminateTaskForm form, String bizType, List<WfFormDataVo> formDatas, WfTaskVo task) {
		WfRuningProcessForm runFrom = new WfRuningProcessForm();
		runFrom.setFormData(formDatas);
		runFrom.setBizTypeCode(bizType);
		runFrom.setUserId(form.getUserId());
		runFrom.setTaskId(form.getTaskId()); //任务ID
		runFrom.setProcInstId(form.getProcInstId()); //流程实例ID
		runFrom.setActivityName(task.getName());
		runFrom.setActivityId(task.getActivityId());
		runFrom.setProcDefId(task.getProcDefId());
		return runFrom;
	}

	/**
	 * 终止日志
	 *
	 * @param form 表单
	 */
	private void terminateProcessLog(WfTerminateTaskForm form, WfTaskVo task) {
		WfLogAddForm logAddForm = new WfLogAddForm();
		logAddForm.setOptType(WfLogOptTypeEnum.TERMINATE.getCode());
		logAddForm.setContent(form.getComment());
		logAddForm.setUser(BaseUtil.getLoginUser().getName());
		logAddForm.setActivityId(task.getActivityId());
		logAddForm.setActivity(task.getName());
		logAddForm.setProcInstId(form.getProcInstId());
		logAddForm.setStartTime(task.getCreateTime());
		logAddForm.setStayTime(new Date());
		this.wfLogService.addWfLog(logAddForm);
	}

	/**
	 * 删除流程实例
	 *
	 * @param procInstId 流程实例ID
	 */
	@Override
	public void deleteProcess(String procInstId) {
		WfFormPo wfForm = this.getFormByProcInstId(procInstId);
		if (!ObjectUtils.isEmpty(wfForm)) {
			List<WfFormDataVo> formDatas = this.getFormDataByFormId(wfForm.getId());
			if (!ObjectUtils.isEmpty(formDatas)) {
				WfBizTypePo wfBizTypePo = this.wfBizTypeService.queryWfTypeByTypeCode(wfForm.getTypeCode()); //根据业务类型代码查询业务配置
				if (!ObjectUtils.isEmpty(wfBizTypePo)) {
					WfRuningProcessForm procForm = this.getWfRunProcForm(procInstId, wfForm.getTypeCode(), formDatas);
					this.callWorkFlowEvent(procForm, wfBizTypePo.getEvent(), "deleteWorkFlowBefore"); //调用任务前事件
					ApiResult result = this.commActivitiService.deleteProcess(procInstId); //调用工作流引擎发起工作流
					if (!result.isSuccess()) {
						throw new BaseException(result.getMessage());
					}
					this.callWorkFlowEvent(procForm, wfBizTypePo.getEvent(), "deleteWorkFlowAfter"); //调用任务前事件
				}
			}
		}
	}

	/**
	 * 得到流程运行表单
	 *
	 * @param procInstId 表单
	 * @param bizType    业务类型
	 * @param formDatas  业务ID
	 * @return 流程运行表单
	 */
	private WfRuningProcessForm getWfRunProcForm(String procInstId, String bizType, List<WfFormDataVo> formDatas) {
		WfRuningProcessForm runFrom = new WfRuningProcessForm();
		runFrom.setFormData(formDatas);
		runFrom.setUserId(FormatUtil.toString(BaseUtil.getLoginUser().getId()));
		runFrom.setBizTypeCode(bizType);
		runFrom.setProcInstId(procInstId); //流程实例ID
		return runFrom;
	}

	/**
	 * 查询流程信息
	 *
	 * @param bizIds  业务ID
	 * @param bizType 业务类型
	 * @return WfProcLogVo
	 */
	@Override
	public List<WfProcLogVo> queryWfProcessLists(List<Integer> bizIds, String bizType) {
		List<WfFormProcVo> wfFormProcVos = this.wfFormDataService.queryProcInstIdByBiz(bizIds, bizType);
		List<String> procInstIds = ListUtil.toValueList(wfFormProcVos, "procInstId", String.class);
		List<WfProcLogVo> wfProcLogVos = new ArrayList<>();
		Map<Integer, UserVo> userVoMap = new HashMap<>();
		if (!ObjectUtils.isEmpty(procInstIds)) {
			ApiResult<List<WfProcLogVo>> apiResult = this.commActivitiService.queryProcessHandleLogList(procInstIds);
			wfProcLogVos = apiResult.getData();
			if (!ObjectUtils.isEmpty(wfProcLogVos)) {
				Set<Integer> userIds = new HashSet();
				for (WfProcLogVo wfProcLogVo : wfProcLogVos) {
					userIds.add(FormatUtil.parseInt(wfProcLogVo.getNewUser()));
				}
				if (!ObjectUtils.isEmpty(userIds)) {
					userVoMap = this.commUserService.getUserVoMapByUserIds(new ArrayList<>(userIds));
				}
				for (WfProcLogVo wfProcLogVo : wfProcLogVos) {
					UserVo user = userVoMap.get(FormatUtil.parseInt(wfProcLogVo.getNewUser()));
					wfProcLogVo.setNewUser(user != null ? user.getName() : "");
				}
			}
		}
		return wfProcLogVos;
	}

	/**
	 * 查询流程日志
	 *
	 * @param procInstId 流程实例ID
	 * @return 流程日志
	 */
	@Override
	public WfLogDetailVo queryWfLogDetailList(String procInstId) {
		ApiResult<WfLogDetailVo> result = this.commActivitiService.queryProcessHandleLogDetail(procInstId);
		WfLogDetailVo wfLogDetailVo = result.getData();
		//获取用户map
		ApiResult<List<SysAllUserVo>> userResult = this.commUserService.queryAllUser();
		List<SysAllUserVo> userVos = userResult.getData();
		Map<String, String> userMap = new HashMap<>();
		if (!ObjectUtils.isEmpty(userVos)) {
			for (SysAllUserVo userVo : userVos) {
				userMap.put(FormatUtil.toString(userVo.getId()), userVo.getActuName());
			}
		}
		//处理vo,传递给前端
		if (!ObjectUtils.isEmpty(wfLogDetailVo)) {
			List<WfProcLogDetailVo> wfProcLogDetailVos = wfLogDetailVo.getWfLog();
			if (!ObjectUtils.isEmpty(wfProcLogDetailVos)) {
				StringBuffer sb = new StringBuffer();
				for (WfProcLogDetailVo wfProcLogDetailVo : wfProcLogDetailVos) {
					wfProcLogDetailVo.setOperateUser(userMap.get(wfProcLogDetailVo.getOperateUser()));
					String nextUserNames = "";
					if (wfProcLogDetailVo.getNextUserName().contains(",")) {
						List<String> userIds = ListUtil.spliceArrayListByStr(wfProcLogDetailVo.getNextUserName(), ",");
						for (String userId : userIds) {
							String nextUserName = userMap.get(userId);
							sb.append(nextUserName).append(",");
						}
						nextUserNames = sb.toString().substring(0, sb.toString().length() - 1);
					} else {
						nextUserNames = userMap.get(wfProcLogDetailVo.getNextUserName());
					}
					wfProcLogDetailVo.setNextUserName(nextUserNames);
				}
			}
		}
		return wfLogDetailVo;
	}

	/**
	 * 查询流程实例
	 *
	 * @param procInstId 流程实例ID
	 * @return 流程实例
	 */
	@Override
	public WfInstVo getWfInstInfoByProcInstId(String procInstId) {
		WfInstVo instVo = null;
		WfFormPo formPo = this.wfFormService.getFormInfoByProcInstId(procInstId);
		if (formPo != null) {
			WfBizTypePo bizTypePo = this.bizTypeService.queryWfTypeByTypeCode(formPo.getTypeCode());
			if (bizTypePo != null) {
				instVo = this.dozerMapper.map(bizTypePo, WfInstVo.class);
				instVo.setId(procInstId);
				instVo.setFormId(formPo.getId());
				List<WfFormDataVo> formDataVos = this.wfFormDataService.queryFormDataVosByFormId(FormatUtil.toString(formPo.getId()));
				instVo.setFormDatas(formDataVos);
			}
		}
		if(instVo == null){
			throw new BaseException("未找到审批业务记录！");
		}
		return instVo;
	}

	/**
	 * 根据流程实例Id获取流程名称
	 *
	 * @param processInstanceId 实例ID
	 * @return 流程实例名称
	 */
	@Override
	public WfProcessInstVo getProcessInstanceById(String processInstanceId) {
		ApiResult<WfProcessInstVo> result = this.commActivitiService.getProcessInstanceById(processInstanceId);
		if (!result.isSuccess()) {
			throw new BaseException(result.getMessage());
		}
		return result.getData();
	}

	/**
	 * 根据流程实例Id获取流程名称
	 *
	 * @param processInstanceId 实例ID
	 * @return 流程实例名称
	 */
	@Override
	public List<WfActivityInstanceVo> getActivityInstanceByProcessInstanceId(String processInstanceId) {
		ApiResult<List<WfActivityInstanceVo>> result = this.commActivitiService.getActivityInstanceByProcessInstanceId(processInstanceId);
		if (!result.isSuccess()) {
			throw new BaseException(result.getMessage());
		}
		return result.getData();
	}

	/**
	 * 根据流程实例Id获取流程名称
	 *
	 * @param activityInstanceId 实例ID
	 * @return 流程实例名称
	 */
	@Override
	public WfActivityInstanceVo getActivityInstanceByActivityInstanceId(String activityInstanceId) {
		ApiResult<WfActivityInstanceVo> result = this.commActivitiService.getActivityInstanceByActivityInstanceId(activityInstanceId);
		if (!result.isSuccess()) {
			throw new BaseException(result.getMessage());
		}
		return result.getData();
	}

	/**
	 * 根据工作项id获得工作项
	 *
	 * @param taskId 任务ID
	 * @return WfTaskVo
	 */
	@Override
	public WfTaskVo getTaskByTaskId(String taskId) {
		ApiResult<WfTaskVo> result = this.commActivitiService.getTaskByTaskId(taskId);
		if (!result.isSuccess()) {
			throw new BaseException(result.getMessage());
		}
		return result.getData();
	}

	/**
	 * 根据流程实例查询任务
	 *
	 * @param processInstanceId 流程实例ID
	 * @return 任务
	 */
	@Override
	public List<WfTaskVo> getRunTaskByProcessInstanceId(String processInstanceId) {
		ApiResult<List<WfTaskVo>> result = this.commActivitiService.getRunTaskByProcessInstanceId(processInstanceId);
		if (!result.isSuccess()) {
			throw new BaseException(result.getMessage());
		}
		return result.getData();
	}

	/**
	 * 根据流程实例查询任务
	 *
	 * @param processInstanceId 流程实例ID
	 * @return 任务
	 */
	@Override
	public List<WfTaskVo> getTaskByProcessInstanceId(String processInstanceId) {
		ApiResult<List<WfTaskVo>> result = this.commActivitiService.getTaskByProcessInstanceId(processInstanceId);
		if (!result.isSuccess()) {
			throw new BaseException(result.getMessage());
		}
		return result.getData();
	}

	/**
	 * 根据流程实例查询任务
	 *
	 * @param processInstanceId 流程实例ID
	 * @return 任务
	 */
	@Override
	public List<WfRunTaskVo> getRunTaskByProcessInstanceId(String processInstanceId, String userId) {
		ApiResult<List<WfRunTaskVo>> result = this.commActivitiService.getRunTaskByProcessInstanceId(processInstanceId, userId);
		if (!result.isSuccess()) {
			throw new BaseException(result.getMessage());
		}
		return result.getData();
	}

	/**
	 * 得到任务候选人
	 * @param taskId 任务
	 * @return 候选人
	 */
	@Override
	public List<String> getTaskCandidates(String taskId){
		ApiResult<List<String>> result = this.commActivitiService.getTaskCandidates(taskId);
		if (!result.isSuccess()) {
			throw new BaseException(result.getMessage());
		}
		return result.getData();
	}
}
