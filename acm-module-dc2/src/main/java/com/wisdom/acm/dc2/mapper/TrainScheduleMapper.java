package com.wisdom.acm.dc2.mapper;

import com.wisdom.acm.dc2.po.TrainSchedulePo;
import com.wisdom.acm.dc2.vo.TrainScheduleVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
@Repository
@Mapper
public interface TrainScheduleMapper extends CommMapper<TrainSchedulePo>
{
    List<TrainScheduleVo> selectByParams(Map<String, Object> mapWhere);

    List<TrainScheduleVo> selectTrainSchedule(Map<String, Object> mapWhere);
}
