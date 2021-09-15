package com.wisdom.acm.dc2.thread;

import com.wisdom.acm.dc2.service.SmsSendService;
import com.wisdom.acm.dc2.service.SmsTaskService;
import com.wisdom.acm.dc2.vo.SmsTaskVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

//扫描定时短信任务
@Component
public class ScanSmsTaskThread implements CommandLineRunner {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private SmsTaskService smsTaskService;

    @Autowired
    private SmsSendService smsSendService;

    @Override
    public void run(String... args) throws Exception {
        while(true){
            List<SmsTaskVo> smsTaskVoList = smsTaskService.scanSmsTaskList();//扫描定时任务
            if(!ObjectUtils.isEmpty(smsTaskVoList)){
                Date now = new Date();
                for(SmsTaskVo sms : smsTaskVoList){
                    Date sendTime = sms.getSendTime();//获取定时发送时间
                    if(!ObjectUtils.isEmpty(sendTime) && sendTime.compareTo(now) <= 0){//当发送时间不为空并且小于当前时间
                        smsTaskService.updateSmsTaskStatus(sms.getId(), 1);
                        smsSendService.sendSms(sms);
                        Thread.sleep(10);//处理完一个任务休眠10毫秒
                    }
                }
            }
            try{
                Thread.sleep(1000);//处理完一轮任务线程休眠1s中
            }catch (InterruptedException e){
                log.error(e.getMessage(), e);
                Thread.sleep(1000);//出现异常线程休眠1s中
            }
        }
    }
}
