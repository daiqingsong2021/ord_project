package com.wisdom.acm.dc3.service;

import com.wisdom.acm.dc3.po.EnergyMonthlyPo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface EnergyMonthlyService extends CommService<EnergyMonthlyPo>
{

    List<EnergyMonthlyPo> selectByParams(Map<String, Object> mapWhere);

}
