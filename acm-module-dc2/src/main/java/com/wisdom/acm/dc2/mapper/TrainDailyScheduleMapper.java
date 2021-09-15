package com.wisdom.acm.dc2.mapper;

import com.wisdom.acm.dc2.po.TrainDailySchedulePo;
import com.wisdom.acm.dc2.vo.TrainDailyScheduleVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface TrainDailyScheduleMapper extends CommMapper<TrainDailySchedulePo>
{
    List<TrainDailyScheduleVo> selectByParams(Map<String, Object> mapWhere);

    List<TrainDailyScheduleVo> selectTrainDailySchedule(Map<String, Object> mapWhere);
}
