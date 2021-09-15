package com.wisdom.acm.processing.service.construction.impl;

import com.wisdom.acm.processing.mapper.construction.ConstructionDailyMapper;
import com.wisdom.acm.processing.po.construction.ConstructionDailyPo;
import com.wisdom.acm.processing.service.construction.ConstructionService;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Map;

/**
 * @author zll
 * 2020/8/25/025 13:53
 * Description:<描述>
 */
@Service
@Slf4j
public class ConstructionServiceImpl extends BaseService<ConstructionDailyMapper, ConstructionDailyPo> implements ConstructionService {
    @Override
    public ConstructionDailyPo queryConstructionDailyPo(Map<String,Object> map) {
        Example example = new Example(ConstructionDailyPo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("line",map.get("line"));
        criteria.andEqualTo("recordTime",map.get("recordTime"));
        ConstructionDailyPo po=this.selectOneByExample(example);
        return po;
    }
}
