package com.wisdom.acm.dc3.service.impl;

import com.wisdom.acm.dc3.mapper.EnergyMonthlyMapper;
import com.wisdom.acm.dc3.po.EnergyMonthlyPo;
import com.wisdom.acm.dc3.service.EnergyMonthlyService;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EnergyMonthlyServiceImpl extends BaseService<EnergyMonthlyMapper, EnergyMonthlyPo> implements EnergyMonthlyService
{


    /**
     * 根据参数查询TrainDailyVo 数据
     * @param mapWhere
     * @return
     */
    @Override
    public List<EnergyMonthlyPo> selectByParams(Map<String, Object> mapWhere) {

        List<EnergyMonthlyPo>  energyMonthlyPoList= mapper.selectByParams(mapWhere);
        return energyMonthlyPoList;
    }






}
