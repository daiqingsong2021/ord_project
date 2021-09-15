package com.wisdom.acm.dc2.mapper;

import com.wisdom.acm.dc2.po.SmsTaskTargetPo;
import com.wisdom.acm.dc2.vo.SmsTaskTargetVo;
import com.wisdom.base.common.mapper.CommMapper;

import java.util.List;

public interface SmsTaskTargetMapper extends CommMapper<SmsTaskTargetPo> {

    //根据任务id查询发送人员的电话号码集合
    List<SmsTaskTargetVo> queryTargetList(Integer taskId);
}
