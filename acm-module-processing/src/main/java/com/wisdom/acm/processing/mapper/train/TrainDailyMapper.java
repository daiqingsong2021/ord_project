package com.wisdom.acm.processing.mapper.train;

import com.wisdom.acm.processing.po.train.TrainDailyPo;
import com.wisdom.acm.processing.vo.train.TrainDailyVo;
import com.wisdom.acm.processing.vo.train.TrainVo;
import com.wisdom.base.common.mapper.CommMapper;

import java.util.List;
import java.util.Map;

public interface TrainDailyMapper extends CommMapper<TrainDailyPo>
{
    List<TrainVo> selectByParams(Map<String, Object> mapWhere);
    TrainDailyVo queryTrain(Map<String, Object> mapWhere);
}
