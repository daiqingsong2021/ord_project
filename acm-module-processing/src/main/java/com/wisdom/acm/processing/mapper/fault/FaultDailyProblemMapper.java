package com.wisdom.acm.processing.mapper.fault;

import com.wisdom.acm.processing.po.fault.FaultDailyProblemPo;
import com.wisdom.acm.processing.vo.fault.FaultDailyProblemDealVo;
import com.wisdom.acm.processing.vo.fault.FaultDailyProblemVo;
import com.wisdom.base.common.mapper.CommMapper;
import java.util.List;
import java.util.Map;

public interface FaultDailyProblemMapper extends CommMapper<FaultDailyProblemPo> {
    List<FaultDailyProblemVo> queryFaultDailyProblem(Map<String,Object> map);
    List<FaultDailyProblemDealVo> queryFaultDailyProblemDeal(Integer id);

    /**
     * 获取新建和处理中的问题列表
     * @param map
     * @return
     */
    List<FaultDailyProblemVo> queryLeftOverProblem(Map<String,Object> map);
}
