package com.wisdom.acm.dc5.mapper;

import com.wisdom.acm.dc5.po.FaultDailyPo;
import com.wisdom.acm.dc5.vo.FaultDailyVo;
import com.wisdom.acm.dc5.vo.FaultMonthlyVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface FaultDailyMapper extends CommMapper<FaultDailyPo> {

    List<FaultDailyVo> selectFaultDailyByParams(Map<String, Object> mapWhere);

    FaultMonthlyVo selectDaily2MonthlyByParams(Map<String, Object> mapWhere);

}
