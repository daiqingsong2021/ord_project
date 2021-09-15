package com.wisdom.acm.activiti.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.wisdom.acm.activiti.service.ModelTestService;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service("modelTestService")
public class ModelTestServiceImpl implements ModelTestService {

	protected static final Logger LOGGER = LoggerFactory.getLogger(ModelTestServiceImpl.class);

	@Autowired
	ProcessEngine processEngine;

	@Autowired
	ObjectMapper objectMapper;

	/**
	 * 获取模板下已发布的流程定义key
	 *
	 * @return
	 */
	@Override
	public String modelProcessDefinition(String modelId) {
		RepositoryService repositoryService = processEngine.getRepositoryService();
		Model model = repositoryService.getModel(modelId);
		if (model == null || StringUtils.isEmpty(model.getDeploymentId())) {
			return null;
		}
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(model.getDeploymentId()).singleResult();
		if (processDefinition == null || StringUtils.isEmpty(processDefinition.getKey())) {
			return null;
		}
		return processDefinition.getKey();

	}

	/**
	 * 我的发起，未完结
	 *
	 * @return
	 */
	@Override
	public List<ProcessInstance> myInitiate(String userId) {
		RuntimeService runtimeService = processEngine.getRuntimeService();
		List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(userId).list();
		return processInstances;
	}

	/**
	 * 我的代办——个人任务
	 *
	 * @return
	 */
	@Override
	public List<Task> myNeedHandlePersonalTask(String userId) {
		TaskService taskService = processEngine.getTaskService();
		List<Task> taskList = taskService.createTaskQuery().taskCandidateOrAssigned(userId).orderByTaskCreateTime().desc().list();
		return taskList;
	}

	/**
	 * 我的代办——可认领任务
	 *
	 * @return
	 */
	@Override
	public List<Task> myNeedHandleClaimTask(List<String> roleNames, String companyId) {
		TaskService taskService = processEngine.getTaskService();
		RepositoryService repositoryService = processEngine.getRepositoryService();
		List<String> modelDeploymentIds = repositoryService.createModelQuery().modelTenantId(companyId).list()
				.stream().map(model -> model.getDeploymentId()).collect(Collectors.toList());
		List<Task> taskList = taskService.createTaskQuery().taskCandidateGroupIn(roleNames).deploymentIdIn(modelDeploymentIds).list();
		return taskList;
	}

	/**
	 * 审批任务——先认领在审批，不然没有权限操作
	 *
	 * @return
	 */
	@Override
	public void approvalTask(String taskId, String userId, String approval) {
		TaskService taskService = processEngine.getTaskService();
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if(task != null) {
			Map<String, Object> map = new HashMap<>();
			//1、认领任务
			taskService.claim(taskId, userId);
			//2.完成任务
			map.put("approval", approval);
			taskService.complete(taskId);
			List<Task> tasks = processEngine.getTaskService().createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
			tasks.stream().forEach(_task -> {
				processEngine.getTaskService().addCandidateUser(_task.getId(), userId);
			});
		}
	}

	/**
	 * 单纯的认领任务，组项目大家都可以进行认领，但是一个人认领后其余人就不可以认领了
	 *
	 * @return
	 */
	@Override
	public void claimTask(String taskId, String userId) {
		TaskService taskService = processEngine.getTaskService();
		//1、认领任务
		taskService.claim(taskId, userId);
	}

	/**
	 * 查询历史完结流程
	 *
	 * @param businessKey
	 * @return
	 */
	@Override
	public List<HistoricProcessInstance> findEndBusinessKey(String businessKey) {
		HistoryService historyService = processEngine.getHistoryService();
		List<HistoricProcessInstance> historicProcessInstanceList = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessKey).finished().list();
		return historicProcessInstanceList;
	}

	/**
	 * 查询某公司全部已完结的流程实例
	 *
	 * @return
	 */
	@Override
	public List<HistoricProcessInstance> queryHistoricInstanceAll(String companyId) {
		HistoryService historyService = processEngine.getHistoryService();
		List<String> deploymentIdList = processEngine.getRepositoryService().createModelQuery().modelTenantId(companyId)
				.list().stream().map(model -> model.getDeploymentId())
				.filter(deploymentId -> StringUtils.isNotEmpty(deploymentId)).collect(Collectors.toList());
		//防止deploymentIdList 没有
		if (deploymentIdList.size() == 0) deploymentIdList.add("xxxxxxxxxxx");
		List<HistoricProcessInstance> historicProcessInstanceList = historyService.createHistoricProcessInstanceQuery().deploymentIdIn(deploymentIdList).finished().list();
		return historicProcessInstanceList;
	}

	/**
	 * 查询某流程实例下的步骤执行过程
	 *
	 * @return
	 */
	@Override
	public List<HistoricActivityInstance> queryHistoricActivitiInstance(String processInstanceId) {
		List<HistoricActivityInstance> list = processEngine.getHistoryService()
				.createHistoricActivityInstanceQuery()
				.processInstanceId(processInstanceId)
				.list();
		return list;
	}

	/**
	 * 查询某流程实例下的任务执行过程
	 *
	 * @return
	 */
	@Override
	public List<HistoricTaskInstance> queryHistoricTask(String processInstanceId) {
		return processEngine.getHistoryService()
				.createHistoricTaskInstanceQuery()
				.processInstanceId(processInstanceId)
				.list();
	}

	/**
	 * 查询某流程实例下执行时设置的流程变量
	 *
	 * @return
	 */
	@Override
	public List<HistoricVariableInstance> queryHistoricVariables(String processInstanceId) {
		return processEngine.getHistoryService()
				.createHistoricVariableInstanceQuery()
				.processInstanceId(processInstanceId)
				.list();
	}

	@Override
	public String taskIdGetProcessInstanceId(String taskId) {
		Task task = processEngine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
//      processEngine.getTaskService().createTaskQuery().taskId()
		if (task == null) {
			return null;
		}
		return task.getProcessInstanceId();
	}

	@Override
	public List<ProcessInstance> queryProcessInstanceByTenantIdAll(String companyId) {
		List<String> deploymentIdList = processEngine.getRepositoryService().createModelQuery().modelTenantId(companyId)
				.list().stream().map(model -> model.getDeploymentId())
				.filter(deploymentId -> StringUtils.isNotEmpty(deploymentId)).collect(Collectors.toList());
		//防止deploymentIdList 没有
		if (deploymentIdList.size() == 0) deploymentIdList.add("xxxxxxxxxxx");
		Set<String> processInstanceIdList = processEngine.getTaskService().createTaskQuery().deploymentIdIn(deploymentIdList).list()
				.stream().map(task -> task.getProcessInstanceId())
				.filter(processInstanceId -> StringUtils.isNotEmpty(processInstanceId)).collect(Collectors.toSet());
		if (processInstanceIdList.size() == 0) processInstanceIdList.add("xxxxxxxxxxx");
		return processEngine.getRuntimeService().createProcessInstanceQuery().processInstanceIds(processInstanceIdList).list();
	}

	/**
	 * 任务退回（取消认领），认领错了退回持有状态，让大家都可以进行认领
	 *
	 * @return
	 */
	@Override
	public void unclaimTask(String taskId) {
		TaskService taskService = processEngine.getTaskService();
		taskService.unclaim(taskId);
	}

}
