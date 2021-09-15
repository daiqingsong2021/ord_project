package com.wisdom.acm.dc2.mapper;

import com.wisdom.acm.dc2.po.TrainDailyPassageMilePo;
import com.wisdom.acm.dc2.po.TrainDailySchedulePo;
import com.wisdom.acm.dc2.vo.TrainDailyPassageMileVo;
import com.wisdom.acm.dc2.vo.TrainDailyScheduleVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface TrainDailyPassageMileMapper extends CommMapper<TrainDailyPassageMilePo>
{
    List<TrainDailyPassageMileVo> selectByParams(Map<String, Object> mapWhere);

}
