package com.wisdom.acm.activiti.listener;

import com.wisdom.base.common.util.WebUtil;
import com.wisdom.base.common.util.calc.calendar.Tools;
import com.wisdom.base.common.vo.wf.WfActivityInstanceVo;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.impl.ActivitiActivityEventImpl;

public class ActivityCompletedListenerHandler implements ActivitiEventListener {

    /**
     * 调用的事件URL后缀
     */
    private static String endsWith = "/wf/listener/complete/activity";

    public ActivityCompletedListenerHandler(){

    }

    public ActivityCompletedListenerHandler(String url){
        this.url = url;
    }

    @Override
    public void onEvent(ActivitiEvent event) {
        ActivitiActivityEventImpl activityEvent = (ActivitiActivityEventImpl)event;
        if(!Tools.isEmpty(this.url)){
            WfActivityInstanceVo activityVo = new WfActivityInstanceVo();
            activityVo.setId(activityVo.getId());
            activityVo.setActivityId(activityEvent.getActivityId());
            activityVo.setActivityName(activityEvent.getActivityName());
            activityVo.setProcessDefinitionId(activityEvent.getProcessDefinitionId());
            activityVo.setProcessInstanceId(activityEvent.getProcessInstanceId());
            activityVo.setActivityType(activityEvent.getActivityType());
            WebUtil.post(this.url + endsWith, activityVo); //调用接口
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
