package com.wisdom.acm.processing.service.train;

import com.wisdom.acm.processing.po.train.TrainSchedulePo;
import com.wisdom.acm.processing.vo.train.ScheduleVo;
import com.wisdom.base.common.service.CommService;
import java.util.Map;

/**
 * @author zll
 * 2020/8/25/025 12:45
 * Description:<描述>
 */
public interface ScheduleService extends CommService<TrainSchedulePo> {
    ScheduleVo queryScheduleVo(Map<String,Object> map);
}
