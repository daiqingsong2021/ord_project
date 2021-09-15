package com.wisdom.acm.dc2.service;

import com.wisdom.acm.dc2.po.SmsTaskTargetPo;
import com.wisdom.acm.dc2.vo.SmsTaskTargetVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;


public interface SmsTaskTargetService extends CommService<SmsTaskTargetPo> {

    //新增任务
    void addTarget(Integer taskId, String mobile);

    //根据任务id查询发送人员的电话号码集合
    List<SmsTaskTargetVo> queryTargetList(Integer taskId);
}
