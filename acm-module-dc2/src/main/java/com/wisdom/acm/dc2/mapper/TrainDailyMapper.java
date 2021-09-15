package com.wisdom.acm.dc2.mapper;

import com.wisdom.acm.dc2.po.TrainDailyPo;
import com.wisdom.acm.dc2.vo.TrainDailyVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface TrainDailyMapper extends CommMapper<TrainDailyPo>
{
    List<TrainDailyVo> selectByParams(Map<String, Object> mapWhere);

    TrainDailyVo selectByMonthRecordTime(Map<String, Object> mapWhere);
}
