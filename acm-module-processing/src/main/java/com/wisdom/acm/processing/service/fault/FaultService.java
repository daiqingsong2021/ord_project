package com.wisdom.acm.processing.service.fault;

import com.wisdom.acm.processing.po.fault.FaultDailyPo;
import com.wisdom.acm.processing.vo.fault.FaultVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

/**
 * @author zll
 * 2020/8/25/025 15:14
 * Description:<描述>
 */
public interface FaultService extends CommService<FaultDailyPo> {
    FaultVo queryFault(Map<String,Object> map);

    /**
     * 获取遗留问题列表
     * @param map
     * @return
     */
    List<FaultVo> queryLeftOverVo(Map<String,Object> map);
}
