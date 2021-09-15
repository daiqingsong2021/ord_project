package com.wisdom.acm.processing.service.construction;

import com.wisdom.acm.processing.po.construction.ConstructionDailyPo;
import com.wisdom.base.common.service.CommService;

import java.util.Map;

/**
 * @author zll
 * 2020/8/25/025 13:52
 * Description:<描述>
 */
public interface ConstructionService extends CommService<ConstructionDailyPo> {
    ConstructionDailyPo queryConstructionDailyPo(Map<String,Object>map);
}
