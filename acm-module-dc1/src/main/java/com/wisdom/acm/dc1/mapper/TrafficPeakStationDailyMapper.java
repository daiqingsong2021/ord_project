package com.wisdom.acm.dc1.mapper;

import com.wisdom.acm.dc1.po.TrafficPeakLineDailyPo;
import com.wisdom.acm.dc1.po.TrafficPeakStationDailyPo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TrafficPeakStationDailyMapper extends CommMapper<TrafficPeakStationDailyPo> {
    void delTrafficPeakStationDailyByDates(@Param("recordTimes") List<String> recordTimes);
}
