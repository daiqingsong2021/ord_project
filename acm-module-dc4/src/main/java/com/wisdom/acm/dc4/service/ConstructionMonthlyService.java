package com.wisdom.acm.dc4.service;


import com.wisdom.acm.dc4.po.ConstructionMonthlyPo;
import com.wisdom.acm.dc4.vo.ConstructionDailyVo;
import com.wisdom.acm.dc4.vo.ConstructionMonthlyVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;


/**
 * ConstructionMonthlyService interface
 * @author  chenhai
 * @data 2020/08/13
 * 施工月况
 */
public interface ConstructionMonthlyService extends CommService<ConstructionMonthlyPo> {

    List<ConstructionMonthlyVo> selectByParams(Map<String, Object> mapWhere);
}
