package com.wisdom.acm.processing.service.energy;

import com.wisdom.acm.processing.po.energy.EnergyDailyPo;
import com.wisdom.acm.processing.vo.energy.EnergyVo;
import com.wisdom.base.common.service.CommService;
import java.util.Map;

/**
 * @author zll
 * 2020/8/25/025 10:47
 * Description:<描述>
 */
public interface EnergyService extends CommService<EnergyDailyPo> {
    EnergyVo queryEnergyVo(Map<String,Object> map);
    EnergyVo queryEnergyDailyVo(Map<String,Object> map);
    EnergyVo queryEnergyMonthlyVo(Map<String,Object> map);
}
