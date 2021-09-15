package com.wisdom.acm.dc1.mapper;

import com.wisdom.acm.dc1.po.TrafficPeakLineDailyPo;
import com.wisdom.acm.dc1.vo.TrafficPeakLineDailyVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TrafficPeakLineDailyMapper extends CommMapper<TrafficPeakLineDailyPo> {
    void delTrafficPeakLineDailyByDates(@Param("recordTimes") List<String> recordTimes);

    List<TrafficPeakLineDailyVo> queryTrafficPeakLineDailyVos(String yearMonth);
}
