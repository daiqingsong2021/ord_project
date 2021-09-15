package com.wisdom.acm.processing.service.train;

import com.wisdom.acm.processing.po.train.TrainDailyPo;
import com.wisdom.acm.processing.vo.train.TrainDailyVo;
import com.wisdom.acm.processing.vo.train.TrainVo;
import com.wisdom.base.common.service.CommService;
import java.util.List;
import java.util.Map;

/**
 * @author zll
 * 2020/8/24/024 9:17
 * Description:<描述>
 */
public interface TrainDailyService extends CommService<TrainDailyPo> {
    List<TrainVo> selectByParams(Map<String, Object> mapWhere);
    TrainDailyVo queryTrain(Map<String, Object> mapWhere);
}
