package com.wisdom.acm.dc1.mapper;

import com.wisdom.acm.dc1.po.TrafficPeakStationMonthlyPo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TrafficPeakStationMonthlyMapper extends CommMapper<TrafficPeakStationMonthlyPo> {
    void delTrafficPeakStationMonthlyByDates(@Param("recordTimes") List<String> recordTimes);
}
