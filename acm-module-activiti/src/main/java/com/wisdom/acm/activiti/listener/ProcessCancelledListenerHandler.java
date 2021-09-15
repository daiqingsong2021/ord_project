package com.wisdom.acm.activiti.listener;

import com.wisdom.base.common.util.WebUtil;
import com.wisdom.base.common.util.calc.calendar.Tools;
import com.wisdom.base.common.vo.wf.WfProcessInstVo;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.impl.ActivitiProcessCancelledEventImpl;

public class ProcessCancelledListenerHandler implements ActivitiEventListener {

    /**
     * 调用的事件URL后缀
     */
    private static String endsWith = "/wf/listener/terminate/workflow";

    public ProcessCancelledListenerHandler(){

    }

    public ProcessCancelledListenerHandler(String url){
        this.url = url;
    }

    @Override
    public void onEvent(ActivitiEvent event) {
        ActivitiProcessCancelledEventImpl entityEvent = (ActivitiProcessCancelledEventImpl)event;
        if(!Tools.isEmpty(this.url)){
            WfProcessInstVo instVo = new WfProcessInstVo();
            instVo.setProcDefId(entityEvent.getProcessDefinitionId());
            instVo.setProcInstId(entityEvent.getProcessInstanceId());
            WebUtil.post(this.url + endsWith, instVo); //调用接口
        }
        System.out.println("ProcessCancelled=" + entityEvent);
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
