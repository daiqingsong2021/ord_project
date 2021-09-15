package com.wisdom.acm.processing.service.fault.impl;

import com.wisdom.acm.processing.mapper.fault.FaultDailyMapper;
import com.wisdom.acm.processing.po.fault.FaultDailyPo;
import com.wisdom.acm.processing.service.fault.FaultService;
import com.wisdom.acm.processing.vo.fault.FaultVo;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author zll
 * 2020/8/25/025 15:15
 * Description:<描述>
 */
@Slf4j
@Service
public class FaultServiceImpl extends BaseService<FaultDailyMapper, FaultDailyPo> implements FaultService {
    @Override
    public FaultVo queryFault(Map<String, Object> map) {
        return mapper.quertFaultVo(map);
    }

    @Override
    public List<FaultVo> queryLeftOverVo(Map<String, Object> map) {
        return mapper.queryLeftOverVo(map);
    }
}
