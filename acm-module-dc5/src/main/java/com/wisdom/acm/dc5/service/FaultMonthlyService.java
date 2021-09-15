package com.wisdom.acm.dc5.service;
import com.wisdom.acm.dc5.po.FaultMonthlyPo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface FaultMonthlyService extends CommService<FaultMonthlyPo> {

    List<FaultMonthlyPo> selectFaultMonthByParams(Map<String, Object> mapWhere);

}
