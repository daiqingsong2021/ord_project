package com.wisdom.acm.activiti.listener;

import com.wisdom.base.common.util.WebUtil;
import com.wisdom.base.common.util.calc.calendar.Tools;
import com.wisdom.base.common.vo.wf.WfProcessInstVo;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

public class ProcessCompletedListenerHandler implements ActivitiEventListener {

    /**
     * 调用的事件URL后缀
     */
    private static String endsWith = "/wf/listener/complete/workflow";

    public ProcessCompletedListenerHandler(){

    }

    public ProcessCompletedListenerHandler(String url){
        this.url = url;
    }

    @Override
    public void onEvent(ActivitiEvent event) {
        ActivitiEntityEvent entityEvent = (ActivitiEntityEvent)event;
        Object entity = entityEvent.getEntity();
        if(!Tools.isEmpty(this.url) && entity instanceof ExecutionEntity){
            ExecutionEntity execut  = (ExecutionEntity) entity;
            WfProcessInstVo instVo = new WfProcessInstVo();
            instVo.setProcDefId(execut.getProcessDefinitionId());
            instVo.setProcDefKey(execut.getProcessDefinitionKey());
            instVo.setProcDefName(execut.getProcessDefinition() != null ? execut.getProcessDefinition().getName() : null);
            instVo.setProcInstId(execut.getProcessInstanceId());
            instVo.setProcInstName(execut.getName());
            WebUtil.post(this.url + endsWith, instVo); //调用接口
        }
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
