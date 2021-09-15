package com.wisdom.acm.processing.service.fault.impl;

import com.wisdom.acm.processing.mapper.fault.FaultDailyProblemMapper;
import com.wisdom.acm.processing.po.fault.FaultDailyProblemPo;
import com.wisdom.acm.processing.service.fault.FaultProblemService;
import com.wisdom.acm.processing.vo.fault.FaultDailyProblemDealVo;
import com.wisdom.acm.processing.vo.fault.FaultDailyProblemVo;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * @author zll
 * 2020/8/25/025 16:37
 * Description:<描述>
 */
@Service
@Slf4j
public class FaultProblemServiceImpl extends BaseService<FaultDailyProblemMapper, FaultDailyProblemPo> implements FaultProblemService {
    @Override
    public List<FaultDailyProblemVo> queryFaultDailyProblem(Map<String, Object> map) {
        return mapper.queryFaultDailyProblem(map);
    }

    @Override
    public List<FaultDailyProblemDealVo> queryFaultDailyProblemDealVo(Integer id) {
        return mapper.queryFaultDailyProblemDeal(id);
    }

    @Override
    public List<FaultDailyProblemVo> queryLeftOverProblem(Map<String, Object> map) {
        return mapper.queryLeftOverProblem(map);
    }
}