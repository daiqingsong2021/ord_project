package com.wisdom.acm.processing.mapper.train;

import com.wisdom.acm.processing.po.train.TrainSchedulePo;
import com.wisdom.acm.processing.vo.train.ScheduleVo;
import com.wisdom.base.common.mapper.CommMapper;
import java.util.Map;

public interface TrainScheduleMapper extends CommMapper<TrainSchedulePo> {
    ScheduleVo queryScheduleVo(Map<String, Object> mapWhere);
}
