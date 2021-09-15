package com.wisdom.acm.dc2.service.impl;

import com.wisdom.acm.dc2.po.message.SmsHistoryDetailsPo;
import com.wisdom.acm.dc2.service.SmsSendService;
import com.wisdom.acm.dc2.service.SmsTaskTargetService;
import com.wisdom.acm.dc2.service.message.SmsHistoryDetailsService;
import com.wisdom.acm.dc2.vo.SmsTaskTargetVo;
import com.wisdom.acm.dc2.vo.SmsTaskVo;
import com.wisdom.base.common.exception.BaseException;
import com.wisdom.base.common.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;

@Service
@Slf4j
public class SmsSendServiceImpl implements SmsSendService {

    @Autowired
    private SmsTaskTargetService smsTaskTargetService;

    @Autowired
    private SmsHistoryDetailsService smsHistoryDetailsService;

    @Override
    public void sendSms(SmsTaskVo task) {
        if(ObjectUtils.isEmpty(task)){
            throw new BaseException("短信任务对象为空");
        }
        int taskId = task.getId();//短信任务Id
        Date sendTime = new Date();//发送时间
        String sendTimeStr = DateUtil.formatDateTime(sendTime);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sendTime);
        calendar.set(Calendar.MINUTE, -10);
        Date tenMinuteAgo = calendar.getTime();//10分钟之前
        String tenMinuteAgoStr = DateUtil.formatDateTime(tenMinuteAgo);//10分钟之前的时间
        List<SmsTaskTargetVo> smsTaskTargetVoList = smsTaskTargetService.queryTargetList(taskId);//获取去重的发送列表集合
        if(!ObjectUtils.isEmpty(smsTaskTargetVoList)){
            smsTaskTargetVoList.stream().forEach(smsTaskTargetVo->{
                int result = 1;//成功
                String number = smsTaskTargetVo.getTargetNumber();
                Map<String,Object> mapWhere = new HashMap<>();
                mapWhere.put("sendStatus", 1);
                mapWhere.put("targetNumber", number);
                mapWhere.put("smsTimeStart",tenMinuteAgoStr);
                mapWhere.put("smsTimeEnd",sendTimeStr);
                int count = smsHistoryDetailsService.targetNumberNum(mapWhere);//查询同一号码10分钟内是否发送超过最大次数
                if(count >= 4){
                    result = 0;//失败
                }
                SmsHistoryDetailsPo smsHistoryDetailsPo = new SmsHistoryDetailsPo();
                smsHistoryDetailsPo.setTaskId(taskId);
                smsHistoryDetailsPo.setSendStatus(String.valueOf(result));
                smsHistoryDetailsPo.setTargetNumber(number);
                smsHistoryDetailsPo.setSmsSendTime(sendTime);
                smsHistoryDetailsService.insertSmsHistoryDetails(smsHistoryDetailsPo);
            });
        }
    }
}
