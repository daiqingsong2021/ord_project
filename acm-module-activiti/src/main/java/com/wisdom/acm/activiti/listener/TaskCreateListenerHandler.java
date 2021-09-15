package com.wisdom.acm.activiti.listener;

import com.wisdom.acm.activiti.service.impl.ActivitiServiceImpl;
import com.wisdom.base.common.util.BaseUtil;
import com.wisdom.base.common.util.JsonUtil;
import com.wisdom.base.common.util.WebUtil;
import com.wisdom.base.common.util.calc.calendar.Tools;
import com.wisdom.base.common.vo.sys.UserVo;
import com.wisdom.base.common.vo.wf.WfActivityVo;
import com.wisdom.base.common.vo.wf.WfCandidateGroupVo;
import com.wisdom.base.common.vo.wf.WfCandidateUserVo;
import com.wisdom.base.common.vo.wf.WfTaskListenerVo;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.IdentityLink;
import org.springframework.util.ObjectUtils;

import java.util.*;

public class TaskCreateListenerHandler implements ActivitiEventListener {

    /**
     * 调用的事件URL后缀
     */
    private static String endsWith = "/wf/listener/create/task";

    public TaskCreateListenerHandler(){

    }

    public TaskCreateListenerHandler(String url){
        this.url = url;
    }

    @Override
    public void onEvent(ActivitiEvent event) {
        ActivitiEntityEvent entityEvent = (ActivitiEntityEvent)event;
        Object entity = entityEvent.getEntity();
        if(entity instanceof TaskEntity){
            TaskEntity task = (TaskEntity) entity;
            String nextActPart =  Tools.toString(task.getVariable("nextActPart"));
            WfActivityVo[] nextActParts = Tools.isEmpty(nextActPart) ? null : JsonUtil.readValue(nextActPart, WfActivityVo[].class);
            //task.removeVariable("nextActPart"); //不能删除防止其它分支拿不到参数
            this.setTaskCandidate(task, nextActParts);
            this.setSender(task); //设置送审者
            this.setListener(task, nextActParts); //设置监听
        }
    }

    /**
     * 设置任务候选人
     * @param task
     * @param nextActParts
     */
    private void setTaskCandidate(TaskEntity task, WfActivityVo[] nextActParts){
        if(!ObjectUtils.isEmpty(nextActParts)) {
            String activityId = task.getExecution().getActivityId();
            Set<String> groupIds = this.getCandidateGroupByActivityId(nextActParts, activityId);
            Set<String> userIds = this.getCandidateUserByActivityId(nextActParts, activityId);
            if(!ObjectUtils.isEmpty(groupIds) || !ObjectUtils.isEmpty(userIds)){
                this.cleanTaskCandidate(task); //清除任务的后选参与者
                task.addCandidateGroups(groupIds);
                task.addCandidateUsers(userIds);
            }
        }
    }

    /**
     * 清除任务的后选参与者
     * @param task
     */
    private void cleanTaskCandidate( TaskEntity task){
        Set<IdentityLink> identityLinks = task.getCandidates();
        if(!ObjectUtils.isEmpty(identityLinks)){
            for(IdentityLink identityLink : identityLinks){
                String userId = identityLink.getUserId();
                if(!ObjectUtils.isEmpty(userId)){
                    task.deleteCandidateUser(userId);
                }
                String groupId = identityLink.getGroupId();
                if(!ObjectUtils.isEmpty(groupId)){
                    task.deleteCandidateGroup(groupId);
                }
            }
        }
    }

    /**
     * 查找活动选择的后续参与者
     * @param nextActParts 后续参与者
     * @param activityId 活动ID
     * @return 候选用户
     */
    private Set<String> getCandidateUserByActivityId(WfActivityVo[] nextActParts, String activityId){
        Set<String> userIds = new HashSet<>();
        if(!Tools.isEmpty(nextActParts)){
            for (WfActivityVo activity : nextActParts){
                if(activityId.equals(activity.getId()) && !Tools.isEmpty(activity.getCandidateUsers())){
                    for (WfCandidateUserVo user : activity.getCandidateUsers()){
                        userIds.add(user.getId());
                    }
                }
            }
        }
        return userIds;
    }

    /**
     * 查找活动选择的后续参与者
     * @param nextActParts 后续参与者
     * @param activityId 活动ID
     * @return 候选组
     */
    private Set<String> getCandidateGroupByActivityId(WfActivityVo[] nextActParts, String activityId){
        Set<String> groupIds = new HashSet<>();
        if(!Tools.isEmpty(nextActParts)){
            for (WfActivityVo activity : nextActParts){
                if(activityId.equals(activity.getId()) && !Tools.isEmpty(activity.getCandidateGroups())){
                    for (WfCandidateGroupVo group : activity.getCandidateGroups()){
                        groupIds.add(group.getId());
                    }
                }
            }
        }
        return groupIds;
    }

    /**
     * 增加任务送审者
     * @param task 任务
     */
    private void setSender(TaskEntity task){
        UserVo user = BaseUtil.getLoginUser();
        if(!Tools.isEmpty(user) && !Tools.isEmpty(user.getCode())){
            Map<String, Object> description = new HashMap<>();
            description.put("sender", user);
            task.setDescription(JsonUtil.toJson(description));
        }
    }

    /**
     * 增加任务送审者
     * @param task 任务
     */
    private void setListener(TaskEntity task, WfActivityVo[] nextActParts){
        if(!Tools.isEmpty(this.url)){
            WfTaskListenerVo taskVo = this.getWfTaskListenerVo(task, nextActParts);
            WebUtil.post(this.url + endsWith, taskVo); //调用接口
        }
    }

    /**
     * 得到待办任务
     * @param task
     * @param nextActParts
     * @return
     */
    private WfTaskListenerVo getWfTaskListenerVo(TaskEntity task, WfActivityVo[] nextActParts){
        UserVo user = BaseUtil.getLoginUser();
        WfTaskListenerVo taskVo = new WfTaskListenerVo();
        taskVo.setId(task.getId());
        taskVo.setProcDefId(task.getProcessDefinitionId());
        taskVo.setProcInstId(task.getProcessInstanceId());
        taskVo.setProcDefKey(task.getExecution() != null ? task.getExecution().getProcessDefinitionKey() : null);
        taskVo.setProcDefName(task.getExecution() != null ? task.getExecution().getProcessDefinition().getName() : null);
        taskVo.setProcInstName(task.getExecution() != null ? task.getExecution().getBusinessKey() : null);
        taskVo.setActivityId(task.getTaskDefinitionKey());
        taskVo.setTaskName(task.getName());
        taskVo.setSender(user);
        taskVo.setCreateTime(task.getCreateTime());
        taskVo.setStart(ActivitiServiceImpl.isStart(task)); //是否发起节点
        taskVo.setEnd(ActivitiServiceImpl.isEnd(task)); //是否批准节点
        this.setTaskCandidate(taskVo, task, nextActParts); //设置候选人
        return taskVo;
    }

    /**
     * 设置任务候选人
     * @param task
     * @param nextActParts
     */
    private void setTaskCandidate(WfTaskListenerVo taskVo, TaskEntity task, WfActivityVo[] nextActParts){
        if(!ObjectUtils.isEmpty(nextActParts)){
            String activityId = task.getExecution().getActivityId();
            taskVo.setCandidateGroups(this.getCandidateGroupVoByActivityId(nextActParts, activityId));
            taskVo.setCandidateUsers(this.getCandidateUserVoByActivityId(nextActParts, activityId));
        } /*else if(taskVo.isStart()){ //发起节点候选人为自已
            List<WfCandidateUserVo> users = new ArrayList<>();
            UserVo user = BaseUtil.getLoginUser();
            users.add(new WfCandidateUserVo(user));
            taskVo.setCandidateUsers(users);
        }*/ else {
            taskVo.setCandidateGroups(this.getCandidateGroupVo(taskVo, task));
            taskVo.setCandidateUsers(this.getCandidateUserVo(taskVo, task));
        }
    }

    /**
     * 查找活动选择的后续参与者
     * @param nextActParts
     * @param activityId
     * @return
     */
    private List<WfCandidateUserVo>  getCandidateUserVoByActivityId(WfActivityVo[] nextActParts, String activityId){
        List<WfCandidateUserVo> users = new ArrayList<>();
        if(!Tools.isEmpty(nextActParts)){
            for (WfActivityVo activity : nextActParts){
                if(activityId.equals(activity.getId()) && !Tools.isEmpty(activity.getCandidateUsers())){
                    users.addAll(activity.getCandidateUsers());
                }
            }
        }
        return users;
    }

    /**
     * 查找活动选择的后续参与者
     * @param nextActParts
     * @param activityId
     * @return
     */
    private List<WfCandidateGroupVo> getCandidateGroupVoByActivityId(WfActivityVo[] nextActParts, String activityId){
        List<WfCandidateGroupVo> groups = new ArrayList<>();
        if(!Tools.isEmpty(nextActParts)){
            for (WfActivityVo activity : nextActParts){
                if(activityId.equals(activity.getId()) && !Tools.isEmpty(activity.getCandidateGroups())){
                    groups.addAll(activity.getCandidateGroups());
                }
            }
        }
        return groups;
    }

    /**
     * 得到候选人
     * @param taskVo 任务
     * @param task 任务
     * @return 候选人
     */
    private List<WfCandidateUserVo> getCandidateUserVo(WfTaskListenerVo taskVo, TaskEntity task){
        List<WfCandidateUserVo> users = new ArrayList<>();
        Set<IdentityLink> identityLinks = task.getCandidates();
        if(!ObjectUtils.isEmpty(identityLinks)){
            for(IdentityLink identityLink : identityLinks){
                String userId = identityLink.getUserId();
                if(!ObjectUtils.isEmpty(userId)){
                    task.deleteCandidateUser(userId);
                    users.add(new WfCandidateUserVo(userId));
                }
            }
        }
        return users;
    }

    /**
     * 设置候选组
     * @param taskVo 任务
     * @param task 任务
     * @return 候选组
     */
    private List<WfCandidateGroupVo> getCandidateGroupVo(WfTaskListenerVo taskVo, TaskEntity task){
        List<WfCandidateGroupVo> groups = new ArrayList<>();
        Set<IdentityLink> identityLinks = task.getCandidates();
        if(!ObjectUtils.isEmpty(identityLinks)){
            for(IdentityLink identityLink : identityLinks){
                String groupId = identityLink.getGroupId();
                if(!ObjectUtils.isEmpty(groupId)){
                    groups.add(new WfCandidateGroupVo(groupId));
                }
            }
        }
        return groups;
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }

    /**
     * 监听调用URL
     */
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
