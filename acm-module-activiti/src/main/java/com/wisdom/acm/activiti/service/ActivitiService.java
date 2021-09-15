package com.wisdom.acm.activiti.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.base.common.form.*;
import com.wisdom.base.common.vo.base.ActModelVo;
import com.wisdom.base.common.vo.wf.*;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.task.TaskInfo;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface ActivitiService {

    /**
     * 获取下一个任务信息
     *
     * @param task 当前任务
     * @return 下一个人工活动
     */
    List<PvmActivity> getNextUserActivity(TaskInfo task);

    /**
     * 得到开始节点的后续活动候选人
     * @param procDefKey
     * @return
     */
    WfCandidateVo getStartNextCandidate(String procDefKey, Map<String, Object> vars);

    /**
     * 得到后续活动候选人
     * @param taskId
     * @return
     */
    WfCandidateVo getNextCandidate(String taskId);

    /**
     * 得到能驳回的活动
     * @param taskId
     * @return
     */
    List<WfActivityVo> getRejectActivity(String taskId);

    /**
     * 发启工作流
     * @param startProcessForm
     * @return
     */
    WfRunProcessVo startProcess(WfStartProcessForm startProcessForm);

    /**
     * 认领任务(处理)
     * @param claimTaskForm
     * @return
     */
    WfClaimVo claimTask(WfClaimTaskForm claimTaskForm);

    /**
     * 完成任务
     * @param completeTaskForm
     */
    WfRunProcessVo completeTask(WfCompleteTaskForm completeTaskForm);

    /**
     * 撤销任务
     * @return
     */
    WfRunProcessVo cancelTask(WfRejectTaskForm rejectTaskForm);

    /**
     * 驳回任务
     * @param rejectTaskForm
     */
    WfRunProcessVo rejectTask(WfRejectTaskForm rejectTaskForm);

    /**
     * 终止工作流
     * @param terminateTaskForm
     */
    void terminateProcess(WfTerminateTaskForm terminateTaskForm);

    /**
     * 删除工作流
     * @param procInstId
     */
    void deleteProcess(String procInstId);

    /**
     * 激活流程
     * @param procInstId
     */
    void activeProcess(String procInstId);

    /**
     * 挂起流程
     * @param
     */
    void suspendProcess(String procInstId);

    /**
     * 通过活动定义ID查询任务信息
     * @param procInstId
     * @param actDefId
     * @return
     */
    List<WfTaskVo> getTaskByActDefId(String procInstId, String actDefId);

    /**
     * 我的待办信息数量
     * @param userId
     * @return
     */
    Long queryWfUnfinishCount(String userId);

    /**
     * 我的待办信息
     * @param userId
     * @param pageSize
     * @param pageNum
     * @param form
     * @return
     */
    PageInfo<MyUnFinishTaskVo> queryMyUnfinishTaskList(String userId, int pageSize, int pageNum, WfUnFinishSearchForm form);

    /**
     * 我的已办信息
     * @param userId
     * @param pageSize
     * @param pageNum
     * @param form
     * @return
     */
    PageInfo<MyFinishTaskVo> queryMyFinishTaskList(String userId, int pageSize, int pageNum, WfFinishSearchForm form);

    /**
     * 我发起的
     * @param userId
     * @param pageSize
     * @param pageNum
     * @param form
     * @return
     */
    PageInfo<WfMineTaskVo> queryWfMineTaskList(String userId, int pageSize, int pageNum, WfMineSearchForm form);

    /**
     * 流程处理信息获取数据
     * @param procInstIds
     * @return
     * @throws ParseException
     */
    List<WfProcLogVo> queryProcessHandleLogList(List<String> procInstIds) throws ParseException;

    /**
     * 流程进展日志
     * @param procInstId
     * @return
     */
    WfLogDetailVo queryProcessHandleLogDetail(String procInstId);

    /**
     * 根据流程ID和用户查询代办
     * @param procInstId
     * @param userId
     * @return
     */
    String queryTaskIdByUserIdAndProcInstrId(String procInstId, String userId);

    /**
     * 查询模型
     * @param modelIds
     * @return
     */
    List<ActModelVo> queryWfAssignList(List<String> modelIds);

    /**
     * 查询流程定义
     * @param modelIds
     * @return
     */
    List<WfProcessDefVo> queryProcDefByModulIdList(List<String> modelIds);

    /**
     * 增加模型
     * @param actModelAddForm
     * @return
     */
    String addModel(ActModelAddForm actModelAddForm);

    /**
     * 发布模型
     * @param modelId 模型ID
     * @return 成功
     * @throws IOException
     */
    String deployProcess(String modelId) throws IOException;

    /**
     * 根据现有模板复制一个新的模型出来
     *
     * @param modelId 模型id
     */
    String copyModel(String modelId);

    /**
     * 根据现有模板复制一个新的模型出来
     *
     * @param modelId  模型id
     * @param tenantId 租户ID
     */
    String copyModel(String modelId, String tenantId);

    /**
     * 删除模型
     * @param modelId 模型ID
     * @return 成功
     */
    String deleteActivityNewModel(String modelId);

    /**
     * 根据流程实例ID查询流程实例
     * @param processInstanceId 流程实例ID
     * @return 流程实例
     */
    WfProcessInstVo getProcessInstanceById(String processInstanceId);

    /**
     * 根据活动实例ID查询流程活动实例
     * @param activityInstanceId 流程活动ID
     * @return 流程活动实例
     */
    WfActivityInstanceVo getActivityInstanceByActivityInstanceId(String activityInstanceId);

    /**
     * 根据流程实例ID查询流程活动实例
     * @param processInstanceId 流程实例ID
     * @return 流程活动实例
     */
    List<WfActivityInstanceVo> getActivityInstanceByProcessInstanceId(String processInstanceId);

    /**
     * 根据任务ID查询任务对象
     * @param taskId 任务ID
     * @return 任务
     */
    WfTaskVo getTaskByTaskId(String taskId);

    /**
     * 得到流程运行中的任务
     * @param processInstanceId 流程实例ID
     * @return 任务
     */
    List<WfTaskVo> getRunTaskByProcessInstanceId(String processInstanceId);

    /**
     * 得到流程的所有任务
     * @param processInstanceId 流程实例ID
     * @return 任务
     */
    public List<WfTaskVo> getTaskByProcessInstanceId(String processInstanceId);

    /**
     * 得到当前用户在流程中的待办
     * @param processInstanceId 流程实例ID
     * @param userId 用户
     * @return 任务
     */
    List<WfRunTaskVo> getRunTaskByProcessInstanceId(String processInstanceId, String userId);

    /**
     * 得到任务的候选人
     * @param taskId
     * @return
     */
    List<String> getTaskCandidates(String taskId);

    /**
     * 查询流程配置URL
     * @return
     */
    String getWfListenerConfigureUrl();
}
