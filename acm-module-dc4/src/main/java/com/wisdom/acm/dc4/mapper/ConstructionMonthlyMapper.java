package com.wisdom.acm.dc4.mapper;


import com.wisdom.acm.dc4.po.ConstructionMonthlyPo;
import com.wisdom.acm.dc4.vo.ConstructionMonthlyVo;
import com.wisdom.base.common.mapper.CommMapper;

import java.util.List;
import java.util.Map;

/**
 * ConstructionDailyMapper class
 * @author  chenhai
 * @data 2020/08/13
 * 施工月况
 */
public interface ConstructionMonthlyMapper extends CommMapper<ConstructionMonthlyPo> {

    List<ConstructionMonthlyVo> selectByParams(Map<String, Object> mapWhere);

}
