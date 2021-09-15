package com.wisdom.acm.processing.mapper.energy;

import com.wisdom.acm.processing.po.energy.EnergyDailyPo;
import com.wisdom.acm.processing.vo.energy.EnergyVo;
import com.wisdom.base.common.mapper.CommMapper;
import java.util.Map;

public interface EnergyDailyMapper extends CommMapper<EnergyDailyPo> {
    EnergyVo queryEnergyVo(Map<String, Object> map);
    EnergyVo queryEnergyDailyVo(Map<String, Object> map);
    EnergyVo queryEnergyMonthlyVo(Map<String, Object> map);
}
