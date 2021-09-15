package com.wisdom.acm.dc4.service.impl;


import com.wisdom.acm.dc4.mapper.ConstructionMonthlyMapper;

import com.wisdom.acm.dc4.po.ConstructionMonthlyPo;

import com.wisdom.acm.dc4.service.ConstructionMonthlyService;
import com.wisdom.acm.dc4.vo.ConstructionMonthlyVo;
import com.wisdom.base.common.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * ConstructionDailyServiceImpl class
 * @author  chenhai
 * @data 2020/08/13
 * 施工月况
 */
@Service
public class ConstructionMonthlyServiceImpl extends BaseService<ConstructionMonthlyMapper, ConstructionMonthlyPo> implements ConstructionMonthlyService {

    @Override
    public List<ConstructionMonthlyVo> selectByParams(Map<String, Object> mapWhere) {
        List<ConstructionMonthlyVo>  constructionMonthlyVoList= mapper.selectByParams(mapWhere);
        return constructionMonthlyVoList;
    }
}
