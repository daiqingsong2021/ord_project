package com.wisdom.acm.processing.service.train.impl;

import com.wisdom.acm.processing.mapper.train.TrainScheduleMapper;
import com.wisdom.acm.processing.po.train.TrainSchedulePo;
import com.wisdom.acm.processing.service.train.ScheduleService;
import com.wisdom.acm.processing.vo.train.ScheduleVo;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.Map;

/**
 * @author zll
 * 2020/8/25/025 12:48
 * Description:<描述>
 */
@Slf4j
@Service
public class ScheduleServiceImpl extends BaseService<TrainScheduleMapper, TrainSchedulePo> implements ScheduleService {
    @Override
    public ScheduleVo queryScheduleVo(Map<String, Object> map) {
        return mapper.queryScheduleVo(map);
    }
}
