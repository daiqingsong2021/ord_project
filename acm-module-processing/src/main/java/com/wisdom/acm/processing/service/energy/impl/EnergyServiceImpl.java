package com.wisdom.acm.processing.service.energy.impl;

import com.wisdom.acm.processing.mapper.energy.EnergyDailyMapper;
import com.wisdom.acm.processing.po.energy.EnergyDailyPo;
import com.wisdom.acm.processing.service.energy.EnergyService;
import com.wisdom.acm.processing.vo.energy.EnergyVo;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Map;

/**
 * @author zll
 * 2020/8/25/025 10:49
 * Description:<描述>
 */
@Service
@Slf4j
public class EnergyServiceImpl extends BaseService<EnergyDailyMapper, EnergyDailyPo> implements EnergyService {
    @Override
    public EnergyVo queryEnergyVo(Map<String, Object> map) {
        return mapper.queryEnergyVo(map);
    }
    @Override
    public EnergyVo queryEnergyDailyVo(Map<String, Object> map) {
        return mapper.queryEnergyDailyVo(map);
    }
    @Override
    public EnergyVo queryEnergyMonthlyVo(Map<String, Object> map) {
        return mapper.queryEnergyMonthlyVo(map);
    }
}
