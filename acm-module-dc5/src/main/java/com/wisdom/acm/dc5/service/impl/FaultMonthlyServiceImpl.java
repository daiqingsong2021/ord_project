package com.wisdom.acm.dc5.service.impl;

import com.wisdom.acm.dc5.mapper.FaultMonthlyMapper;
import com.wisdom.acm.dc5.po.FaultMonthlyPo;
import com.wisdom.acm.dc5.service.FaultMonthlyService;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FaultMonthlyServiceImpl extends BaseService<FaultMonthlyMapper, FaultMonthlyPo> implements FaultMonthlyService
{


    /**
     * 根据参数查询TrainDailyVo 数据
     * @param mapWhere
     * @return
     */
    @Override
    public List<FaultMonthlyPo> selectFaultMonthByParams(Map<String, Object> mapWhere) {
        List<FaultMonthlyPo>  faultMonthlyPoList= mapper.selectFaultMonthByParams(mapWhere);
        return faultMonthlyPoList;
    }






}
