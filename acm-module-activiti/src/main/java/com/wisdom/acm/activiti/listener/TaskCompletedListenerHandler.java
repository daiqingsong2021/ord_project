package com.wisdom.acm.activiti.listener;

import com.wisdom.acm.activiti.service.impl.ActivitiServiceImpl;
import com.wisdom.base.common.util.BaseUtil;
import com.wisdom.base.common.util.FormatUtil;
import com.wisdom.base.common.util.WebUtil;
import com.wisdom.base.common.util.calc.calendar.Tools;
import com.wisdom.base.common.vo.sys.UserVo;
import com.wisdom.base.common.vo.wf.WfTaskListenerVo;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

import java.util.Date;

public class TaskCompletedListenerHandler implements ActivitiEventListener {

    /**
     * 调用的事件URL后缀
     */
    private static String endsWith = "/wf/listener/complete/task";

    public TaskCompletedListenerHandler(){

    }

    public TaskCompletedListenerHandler(String url){
        this.url = url;
    }

    @Override
    public void onEvent(ActivitiEvent event) {
        ActivitiEntityEvent entityEvent = (ActivitiEntityEvent)event;
        Object entity = entityEvent.getEntity();
        if(entity instanceof TaskEntity){
            TaskEntity task = (TaskEntity) entity;
            this.setListener(task); //设置监听
        }
    }

    /**
     * 增加任务送审者
     * @param task 任务
     */
    private void setListener(TaskEntity task){
        if(!Tools.isEmpty(this.url)){
            WfTaskListenerVo taskVo = this.getWfTaskListenerVo(task);
            WebUtil.post(this.url + endsWith, taskVo); //调用接口
        }
    }

    /**
     * 得到待办任务
     * @param task
     * @return
     */
    private WfTaskListenerVo getWfTaskListenerVo(TaskEntity task){
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
        taskVo.setEndTime(new Date());
        taskVo.setStart(ActivitiServiceImpl.isStart(task)); //是否发起节点
        taskVo.setEnd(ActivitiServiceImpl.isEnd(task)); //是否批准节点
        return taskVo;
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
