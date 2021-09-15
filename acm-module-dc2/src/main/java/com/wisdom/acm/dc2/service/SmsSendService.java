package com.wisdom.acm.dc2.service;

import com.wisdom.acm.dc2.vo.SmsTaskVo;

public interface SmsSendService {

    //发送短信任务
    void sendSms(SmsTaskVo task);
}
