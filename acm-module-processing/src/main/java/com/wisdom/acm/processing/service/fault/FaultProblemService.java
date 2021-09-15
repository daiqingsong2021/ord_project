package com.wisdom.acm.processing.service.fault;

import com.wisdom.acm.processing.po.fault.FaultDailyProblemPo;
import com.wisdom.acm.processing.vo.fault.FaultDailyProblemDealVo;
import com.wisdom.acm.processing.vo.fault.FaultDailyProblemVo;
import com.wisdom.base.common.service.CommService;
import java.util.List;
import java.util.Map;

/**
 * @author zll
 * 2020/8/25/025 16:36
 * Description:<描述>
 */
public interface FaultProblemService extends CommService<FaultDailyProblemPo> {
    List<FaultDailyProblemVo> queryFaultDailyProblem(Map<String,Object> map);
    List<FaultDailyProblemDealVo>queryFaultDailyProblemDealVo(Integer id);

    /**
     * 获取新建和处理中的问题列表
     * @param map
     * @return
     */
    List<FaultDailyProblemVo> queryLeftOverProblem(Map<String,Object> map);
}
