package com.wisdom.acm.processing.service.train.impl;

import com.wisdom.acm.processing.mapper.train.TrainDailyMapper;
import com.wisdom.acm.processing.po.train.TrainDailyPo;
import com.wisdom.acm.processing.service.train.TrainDailyService;
import com.wisdom.acm.processing.vo.train.TrainDailyVo;
import com.wisdom.acm.processing.vo.train.TrainVo;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * @author zll
 * 2020/8/24/024 9:24
 * Description:<描述>
 */
@Service
@Slf4j
public class TrainDailyServiceImpl extends BaseService<TrainDailyMapper, TrainDailyPo> implements TrainDailyService {
    @Override
    public List<TrainVo> selectByParams(Map<String, Object> mapWhere) {
        List<TrainVo> trainVos=mapper.selectByParams(mapWhere);
        return trainVos;
    }

    @Override
    public TrainDailyVo queryTrain(Map<String, Object> mapWhere) {
        return mapper.queryTrain(mapWhere);
    }
}
