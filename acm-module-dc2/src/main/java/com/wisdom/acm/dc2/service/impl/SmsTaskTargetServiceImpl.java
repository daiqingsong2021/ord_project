package com.wisdom.acm.dc2.service.impl;

import com.wisdom.acm.dc2.mapper.SmsTaskTargetMapper;
import com.wisdom.acm.dc2.po.SmsTaskTargetPo;
import com.wisdom.acm.dc2.service.SmsTaskTargetService;
import com.wisdom.acm.dc2.vo.SmsTaskTargetVo;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class SmsTaskTargetServiceImpl extends BaseService<SmsTaskTargetMapper, SmsTaskTargetPo> implements SmsTaskTargetService {

    @Override
    public void addTarget(Integer taskId, String mobile) {
        SmsTaskTargetPo insertPo = new SmsTaskTargetPo();
        insertPo.setTaskId(taskId);
        insertPo.setTargetNumber(mobile);
        super.insert(insertPo);
    }

    @Override
    public List<SmsTaskTargetVo> queryTargetList(Integer taskId) {
        return mapper.queryTargetList(taskId);
    }
}
