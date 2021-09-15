package com.wisdom.acm.dc4.mapper;

import com.wisdom.acm.dc4.po.ConstructionDailyPo;
import com.wisdom.acm.dc4.vo.ConstructionDailyRcVo;
import com.wisdom.acm.dc4.vo.ConstructionDailyVo;
import com.wisdom.base.common.mapper.CommMapper;

import java.util.List;
import java.util.Map;
/**
 * ConstructionDailyMapper class
 * @author  chenhai
 * @data 2020/08/13
 * 施工日况
 */

public interface ConstructionDailyMapper extends CommMapper<ConstructionDailyPo> {
    /**
     * 根据线路查询施工日况
     * @param mapWhere
     * @return
     */
    List<ConstructionDailyPo> selectConstructionDailyPo(Map<String, Object> mapWhere);

    List<ConstructionDailyVo> selectByParams(Map<String, Object> mapWhere);
    String querySumWeekPlanNum(Map<String, Object> mapWhere);

    ConstructionDailyVo selectDaily2MonthByParams(Map<String, Object> mapWhere);

    List<Integer> getConstructionDailyByRcIds();

    List<ConstructionDailyRcVo> queryConstructionDailyExitsByid();
}
