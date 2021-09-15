package com.wisdom.acm.szxm.service.wf.impl;

import com.wisdom.acm.szxm.mapper.wf.WfMapper;
import com.wisdom.acm.szxm.service.wf.WfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WfServiceImpl implements WfService
{
    @Autowired
    private WfMapper wfMapper;

    @Override public Integer getProcCreatorByProcInstId(String instanceId)
    {
        return wfMapper.getProcCreatorByProcInstId(instanceId);
    }
}
