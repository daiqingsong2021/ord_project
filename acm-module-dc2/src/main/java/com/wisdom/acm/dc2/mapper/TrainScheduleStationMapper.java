package com.wisdom.acm.dc2.mapper;

import com.wisdom.acm.dc2.po.TrainScheduleStationPo;
import com.wisdom.acm.dc2.vo.TrainScheduleStationVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface TrainScheduleStationMapper extends CommMapper<TrainScheduleStationPo>
{
    List<TrainScheduleStationVo> selectByParams(Map<String, Object> mapWhere);

    List<TrainScheduleStationVo> selectTrainScheduleStation(Map<String, Object> mapWhere);
}
