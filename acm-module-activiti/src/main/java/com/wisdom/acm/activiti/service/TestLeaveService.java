package com.wisdom.acm.activiti.service;

import com.wisdom.acm.activiti.util.canvas.CustomProcessDiagramGeneratorI;
import com.wisdom.acm.activiti.util.canvas.WorkflowConstants;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service("leaveService")
public class TestLeaveService {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private CustomProcessDiagramGeneratorI customProcessDiagramGeneratorI;

	@Autowired
	SpringProcessEngineConfiguration processEngineConfiguration;

	/**
	 * 启动流程
	 * 
	 */
	public void startProcess(String bizKey) {
		//第一个参数是指定启动流程的id,即要启动哪个流程 ;第二个参数是指业务id
		System.out.println("启动前-----");
		runtimeService.startProcessInstanceByKey("leaveProcess", bizKey);
		System.out.println("启动之后------");
	}
	
	/**
	 * 根据审批人id查询需要审批的任务
	 * @param userId
	 * @return
	 */
	public List<Map<String,String>> findTaskByUserId(String userId) {
		List<Map<String,String>> list = new ArrayList<>();
		try {
			List<Task> taskList   = taskService.createTaskQuery().taskCandidateOrAssigned(userId).list();
			for (Task task : taskList) {
				ProcessInstance result = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
				//获得业务流程的bussinessKey
				String businessKey = result.getBusinessKey();
				Map<String,String> map= new HashMap(){{
					put("taskId",task.getId());
					put("ProcessInstanceBusinessKey",businessKey);
				}};
				list.add(map);
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		return list;
	}


	/**
	 * 发起人查询自己尚未完结的请求
	 * @param businessKey
	 * @return
	 */
	public List<Map<String,String>> findBusinessKey(String businessKey) {
		List<Map<String,String>> list = new ArrayList<>();
		try {
			List<ProcessInstance> result = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).list();
			for (ProcessInstance processInstance : result) {
				HistoricProcessInstance historicProcessInstance =  historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstance.getId()).singleResult();
				//获得业务流程的bussinessKey
				Map<String,String> map= new HashMap(){{
					put("processInstanceId",processInstance.getId());
					put("businessKey",processInstance.getBusinessKey());
					if(historicProcessInstance != null && historicProcessInstance.getStartTime() != null && historicProcessInstance.getEndTime() != null) {
						put("isEnd",true);
					}else {
						put("isEnd",false);
					}
				}};
				list.add(map);
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		return list;
	}


	/**
	 * 发起人查询组审批的请求
	 * @param
	 * @return
	 */
	public List<Map<String,String>> findGroupTask(String groupId) {
		List<Map<String,String>> list = new ArrayList<>();
		try {
			List<Task> taskList = taskService.createTaskQuery().taskCandidateGroup(groupId).list();
			for (Task task : taskList) {
				ProcessInstance result = runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
				//获得业务流程的bussinessKey
				String businessKey = result.getBusinessKey();
				Map<String,String> map= new HashMap(){{
					put("taskId",task.getId());
					put("ProcessInstanceBusinessKey",businessKey);
				}};
				list.add(map);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return list;
	}


	/**
	 * 发起人查询自己已完结的请求
	 * @param businessKey
	 * @return
	 */
	public List<Map<String,String>> findEndBusinessKey(String businessKey) {
		List<Map<String,String>> list = new ArrayList<>();
		try {
			List<HistoricProcessInstance> historicProcessInstanceList = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessKey).list();
			for (HistoricProcessInstance historicProcessInstance : historicProcessInstanceList) {
				//获得业务流程的bussinessKey
				Map<String,String> map= new HashMap(){{
					put("processInstanceId",historicProcessInstance.getId());
					put("businessKey",historicProcessInstance.getBusinessKey());
					put("startTime",historicProcessInstance.getStartTime());
					put("endTime",historicProcessInstance.getEndTime());
					put("startUserId",historicProcessInstance.getStartUserId());
				}};
				list.add(map);
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 审批
	 * @param taskId 审批的任务id
	 * @param userId 审批人的id
	 * @param audit  审批意见：通过（pass）or驳回（reject）
	 */
	 public void completeTaskByUser(String taskId, String userId, String audit) {
		 Map<String, Object> map = new HashMap<>();
		 //1、认领任务
		 taskService.claim(taskId, userId);
		//2.完成任务
		 map.put("approval", audit);
		 taskService.complete(taskId, map);
	 }
	
	/**
	 * 查询相关的项目经理
	 * @param execution 执行实例的代理对象 ,代表的是一个请假的具体实例
	 * @return
	 */
	public List<String> findProjectManager(DelegateExecution execution) {
		return Arrays.asList("project1","project2");
	}
	
	/**
	 * 查询相关的人事经理
	 * @param execution 执行实例的代理对象,代表的是一个请假的具体实例
	 * @return
	 */
	public List<String> findHrManager(DelegateExecution execution) {
		return Arrays.asList("hr1","hr2");
	}

	/**
	 * 根据流程id查看该流程是否结束
	 * @param processInstanceId
	 * @return
	 */
	public boolean queryProcessIsEnd(String processInstanceId){
		
		HistoricProcessInstance result = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		if(result != null && result.getStartTime() != null && result.getEndTime() != null) {
			return true;
		}
		return false;
	}
	
	/**
	 * 根据任务id获取流程实例
	 * @param taskId
	 * @return
	 */
	public ProcessInstance findProcessInstanceByTaskId(String taskId) {
		  ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(findTaskEntityById(taskId).getProcessInstanceId()).singleResult();
		  return processInstance;

	}

	/**
	 * 根据任务id获取流程定义
	 * @param taskId
	 * @return
	 */
	public ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(String taskId) {
		
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(findTaskEntityById(taskId).getProcessDefinitionId());
		return processDefinition;
	}
	
	/**
	 * 根据任务id获取任务实例
	 * @param taskId
	 * @return
	 */
	public TaskEntity findTaskEntityById(String taskId) {
		return (TaskEntity) taskService.createTaskQuery().taskId(taskId).singleResult();
	}
	
//	/**
//	 * 获取流程图
//	 * @param processDefId
//	 * @return
//	 */
	public  InputStream findProcessPic(String processDefId) {
		ProcessDefinition result = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefId).singleResult();
		String name = result.getDiagramResourceName();
		InputStream inputStream = repositoryService.getResourceAsStream(result.getDeploymentId(), name);
		return inputStream;
	}

	/**
	 * 读取动态流程图
	 * @param procInstId
	 * @return
	 * @throws IOException
	 */
	public InputStream readProcessImg(String procInstId) throws IOException {
		HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(procInstId).singleResult();
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
		ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processInstance.getProcessDefinitionId());
		List<HistoricActivityInstance> highLightedActivitList = historyService.createHistoricActivityInstanceQuery().processInstanceId(procInstId).orderByHistoricActivityInstanceStartTime().asc().list();
		// 高亮环节id集合
		List<String> highLightedActivitis = new ArrayList<String>();
		// 高亮线路id集合
		List<String> highLightedFlows = getHighLightedFlows(definitionEntity, highLightedActivitList);
		for (HistoricActivityInstance tempActivity : highLightedActivitList) {
			String activityId = tempActivity.getActivityId();
			highLightedActivitis.add(activityId);
		}
		Set<String> currIds = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).list().stream().map(e->e.getActivityId()).collect(Collectors.toSet());
		CustomProcessDiagramGeneratorI diagramGenerator = (CustomProcessDiagramGeneratorI) processEngineConfiguration.getProcessDiagramGenerator();
		InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivitis, highLightedFlows,
				WorkflowConstants.WORKLOW_FONT_NAME, WorkflowConstants.WORKLOW_FONT_NAME, WorkflowConstants.WORKLOW_FONT_NAME, null, 1.0,
				new Color[] { WorkflowConstants.COLOR_NORMAL, WorkflowConstants.COLOR_CURRENT }, currIds);
		return imageStream;
	}

	/**
	 * 获取需要高亮的线
	 *
	 * @param processDefinitionEntity
	 * @param historicActivityInstances
	 * @return
	 */
	private List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinitionEntity, List<HistoricActivityInstance> historicActivityInstances) {
		List<String> highFlows = new ArrayList<>();// 用以保存高亮的线flowId
		for (int i = 0; i < historicActivityInstances.size() - 1; i++) {// 对历史流程节点进行遍历
			ActivityImpl activityImpl = processDefinitionEntity.findActivity(historicActivityInstances.get(i).getActivityId());// 得到节点定义的详细信息
			List<ActivityImpl> sameStartTimeNodes = new ArrayList<>();// 用以保存后需开始时间相同的节点
			ActivityImpl sameActivityImpl1 = processDefinitionEntity.findActivity(historicActivityInstances.get(i + 1).getActivityId());
			// 将后面第一个节点放在时间相同节点的集合里
			sameStartTimeNodes.add(sameActivityImpl1);
			for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
				HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);// 后续第一个节点
				HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);// 后续第二个节点
				if (Math.abs(activityImpl1.getStartTime().getTime()-activityImpl2.getStartTime().getTime()) < 200) {
//                    if (activityImpl1.getStartTime().equals(activityImpl2.getStartTime())) {
					// 如果第一个节点和第二个节点开始时间相同保存
					ActivityImpl sameActivityImpl2 = processDefinitionEntity.findActivity(activityImpl2.getActivityId());
					sameStartTimeNodes.add(sameActivityImpl2);
				} else {
					break; // 有不相同跳出循环
				}
			}
			List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();// 取出节点的所有出去的线
			for (PvmTransition pvmTransition : pvmTransitions) {
				// 对所有的线进行遍历
				ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition.getDestination();
				// 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
				if (sameStartTimeNodes.contains(pvmActivityImpl)) {
					highFlows.add(pvmTransition.getId());
				}
			}
		}
		return highFlows;
	}
}
