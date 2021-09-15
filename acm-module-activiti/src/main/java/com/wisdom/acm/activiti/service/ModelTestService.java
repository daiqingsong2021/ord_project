package com.wisdom.acm.activiti.service;


import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface ModelTestService {



	/**
	 * 获取模板下已发布的流程定义key
	 *
	 * @return
	 */
	String modelProcessDefinition(String modelId);

	/**
	 * 我的发起，未完结
	 *
	 * @return
	 */
	List<ProcessInstance> myInitiate(String userId);

	/**
	 * 我的代办——个人任务
	 *
	 * @return
	 */
	List<Task> myNeedHandlePersonalTask(String userId);


	/**
	 * 我的代办——可认领任务
	 *
	 * @return
	 */
	List<Task> myNeedHandleClaimTask(List<String> roleNames, String companyId);

	/**
	 * 审批任务——组任务需要先认领在审批，不然没有权限操作
	 *
	 * @return
	 */
	void approvalTask(String taskId, String userId, String approval);

	/**
	 * 单纯的认领任务，组项目大家都可以进行认领，但是一个人认领后其余人就不可以认领了
	 *
	 * @return
	 */
	void claimTask(String taskId, String userId);

	/**
	 * 发起人查询自己已完结的请求
	 *
	 * @param businessKey
	 * @return
	 */
	List<HistoricProcessInstance> findEndBusinessKey(String businessKey);

	/**
	 * 查询某公司全部已完结的流程实例
	 *
	 * @return
	 */
	List<HistoricProcessInstance> queryHistoricInstanceAll(String companyId);

	/**
	 * 查询某流程实例下的步骤执行过程
	 *
	 * @return
	 */
	List<HistoricActivityInstance> queryHistoricActivitiInstance(String processInstanceId);

	/**
	 * 查询某流程实例下的任务执行过程
	 *
	 * @return
	 */
	List<HistoricTaskInstance> queryHistoricTask(String processInstaceId);

	/**
	 * 查询某流程实例下执行时设置的流程变量
	 *
	 * @return
	 */
	List<HistoricVariableInstance> queryHistoricVariables(String processInstanceId);

	/**
	 * 查询某流程实例下执行时设置的流程变量
	 *
	 * @return ProcessInstanceId
	 */
	String taskIdGetProcessInstanceId(String taskId);

	/**
	 * 查询某公司正在进行的流程实例 （task正常情况下只存在一个）
	 *
	 * @return
	 */
	List<ProcessInstance> queryProcessInstanceByTenantIdAll(String companyId);

	/**
	 * 任务退回，认领错了退回持有状态，让大家都可以进行认领
	 *
	 * @return
	 */
	void unclaimTask(String taskId);

}
