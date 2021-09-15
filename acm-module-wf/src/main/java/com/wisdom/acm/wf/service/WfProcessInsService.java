package com.wisdom.acm.wf.service;

import com.wisdom.acm.wf.vo.WfInstVo;
import com.wisdom.base.common.form.*;
import com.wisdom.base.common.vo.wf.*;

import java.util.List;

public interface WfProcessInsService {

    /**
     * 得到开始节点的后续活动候选人
     * @param userId 用户
     * @param bizType 业务代码
     * @param procDefKey 流程定义key
     * @return 流程参与者
     */
    WfCandidateVo getStartNextCandidate(String userId, String bizType, String procDefKey, WfStartProcessForm form);

    /**
     * 得到开始节点的后续活动候选人
     * @param userId 用户
     * @param bizType 业务代码
     * @param procDefKey 流程定义key
     * @return 流程参与者
     */
    List<WfCandidateTreeVo> getStartNextCandidateTree(String userId, String bizType, String procDefKey, WfStartProcessForm form);

    /**
     * 得到后续活动候选人
     * @param userId 用户
     * @param taskId 流程定义key
     * @return 流程参与者
     */
    WfCandidateVo getNextCandidate(String userId, String taskId);

    /**
     * 得到后续活动候选人
     * @param userId 用户
     * @param taskId 流程定义key
     * @return 流程参与者
     */
    List<WfCandidateTreeVo> getNextCandidateTree(String userId, String taskId);

    /**
     * 得到能驳回的活动
     * @param taskId 任务ID
     * @return 活动VO
     */
    List<WfActivityVo> getRejectActivity(String taskId);

    /**
     * 发起工作流
     * @param form 表单
     * @return 流程实例
     */
    WfRunProcessVo startProcess(WfStartProcessForm form);

    /**
     * 认领任务
     * @param form 表单
     * @return WfClaimVo
     */
    WfClaimVo claimTask(WfClaimTaskForm form);

    /**
     * 完成任务
     * @param from 表单
     * @param task 任务
     * @return 流程运行VO
     */
    WfRunProcessVo completeTask(WfCompleteTaskForm from, WfTaskVo task);

    /**
     * 撤销任务
     * @param form 表单
     * @param task 任务
     * @return 流程运行VO
     */
    WfRunProcessVo cancelTask(WfRejectTaskForm form, WfTaskVo task);

    /**
     * 驳回任务
     * @param form 表单
     * @param task 任务
     * @return 流程运行VO
     */
    WfRunProcessVo rejectTask(WfRejectTaskForm form, WfTaskVo task);

    /**
     * 终止流程实例
     * @param form 表单
     */
    void terminateProcess(WfTerminateTaskForm form, WfTaskVo task);

    /**
     * 删除流程实例
     * @param procInstId 流程实例ID
     */
    void deleteProcess(String procInstId);

    /**
     * 查询业务日志
     * @param bizIds 业务ID
     * @param bizType 业务类型
     * @return 业务日志
     */
    List<WfProcLogVo> queryWfProcessLists(List<Integer> bizIds, String bizType);

    /**
     * 查询进展日志
     * @param procInstId 流程实例ID
     * @return 进展日志
     */
    WfLogDetailVo queryWfLogDetailList(String procInstId);

    /**
     * 根据流程实例获取流程信息
     *
     * @param procInstId 流程实例ID
     * @return 流程实例
     */
    WfInstVo getWfInstInfoByProcInstId(String procInstId);

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
     * 得到流程运行中任务
     * @param processInstanceId
     * @return
     */
    List<WfTaskVo> getRunTaskByProcessInstanceId(String processInstanceId);

    /**
     * 得到流程的所有任务
     * @param processInstanceId 流程实例ID
     * @return 任务
     */
    List<WfTaskVo> getTaskByProcessInstanceId(String processInstanceId);

    /**
     * 得到当前用户在流程中的待办
     * @param processInstanceId 流程实例ID
     * @param userId 用户
     * @return 任务
     */
    List<WfRunTaskVo> getRunTaskByProcessInstanceId(String processInstanceId, String userId);

    /**得到后选人
     *
     * @param taskId
     * @return
     */
    List<String> getTaskCandidates(String taskId);
}
