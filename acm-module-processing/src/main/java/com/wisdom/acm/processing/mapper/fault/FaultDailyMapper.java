package com.wisdom.acm.processing.mapper.fault;

import com.wisdom.acm.processing.po.fault.FaultDailyPo;
import com.wisdom.acm.processing.vo.fault.FaultVo;
import com.wisdom.base.common.mapper.CommMapper;

import java.util.List;
import java.util.Map;

public interface FaultDailyMapper extends CommMapper<FaultDailyPo> {
    FaultVo quertFaultVo(Map<String,Object> map);

    /**
     * 获取遗留问题列表
     * @param map
     * @return
     */
    List<FaultVo> queryLeftOverVo(Map<String,Object> map);
}
