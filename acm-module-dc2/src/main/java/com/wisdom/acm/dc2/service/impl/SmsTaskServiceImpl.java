package com.wisdom.acm.dc2.service.impl;

import com.wisdom.acm.dc2.form.SmsTaskAddForm;
import com.wisdom.acm.dc2.mapper.SmsTaskMapper;
import com.wisdom.acm.dc2.po.SmsTaskPo;
import com.wisdom.acm.dc2.po.SmsTaskTargetPo;
import com.wisdom.acm.dc2.service.SmsSendService;
import com.wisdom.acm.dc2.service.SmsTaskService;
import com.wisdom.acm.dc2.service.SmsTaskTargetService;
import com.wisdom.acm.dc2.vo.SmsTaskVo;
import com.wisdom.base.common.feign.LeafService;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class SmsTaskServiceImpl extends BaseService<SmsTaskMapper, SmsTaskPo> implements SmsTaskService {

    private Executor threadPool = Executors.newFixedThreadPool(5);//线程池

    @Autowired
    private SmsTaskTargetService smsTaskTargetService;
    @Autowired
    private LeafService leafService;
    @Autowired
    private SmsSendService smsSendService;

    @Override
    public void addSmsTask(SmsTaskAddForm form) {
        List<SmsTaskTargetPo> smsTaskTargetPos = form.getMobiles();
        SmsTaskPo smsTaskPo = dozerMapper.map(form, SmsTaskPo.class);
        Integer id = leafService.getId();
        smsTaskPo.setId(id);
        smsTaskPo.setSmsSendStatus(0);
        super.insert(smsTaskPo);
        if(!ObjectUtils.isEmpty(smsTaskTargetPos)){
            smsTaskTargetPos.stream().forEach(item->{
                smsTaskTargetService.addTarget(id, item.getTargetNumber());
            });
        }
        //直接发送短信任务
        int sendType = smsTaskPo.getSendTimeWay();
        if(sendType == 1){
            addThread(id);
        }
    }

    @Override
    public void delSmsTask(List<Integer> ids) {
        super.deleteByIds(ids);
    }

    @Override
    public List<SmsTaskVo> querySmsTaskList(Map<String, Object> mapWhere) {
        return mapper.querySmsTaskList(mapWhere);
    }

    @Override
    public List<SmsTaskVo> scanSmsTaskList() {
        return mapper.scanSmsTaskList();
    }

    @Override
    public void updateSmsTaskStatus(Integer taskId, Integer status) {
        SmsTaskPo updatePo = new SmsTaskPo();
        updatePo.setId(taskId);
        updatePo.setSmsSendStatus(status);
        super.updateSelectiveById(updatePo);
    }

    //异步处理发送任务
    private void addThread(final Integer taskId){
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                SmsTaskPo smsTaskPo = SmsTaskServiceImpl.super.selectById(taskId);//根据主键查询任务Id
                if(!ObjectUtils.isEmpty(smsTaskPo)){
                    SmsTaskVo smsTaskVo = dozerMapper.map(smsTaskPo,SmsTaskVo.class);
                    if(!ObjectUtils.isEmpty(smsTaskVo)){
                        updateSmsTaskStatus(taskId, 1);
                        smsSendService.sendSms(smsTaskVo);
                    }
                }
            }
        });
    }
}
