package com.wisdom.acm.dc1.service;

import com.wisdom.acm.dc1.po.TrafficPeakStationDailyPo;
import com.wisdom.base.common.service.CommService;
import java.util.List;

public interface TrafficPeakStationDailyService extends CommService<TrafficPeakStationDailyPo> {
    //根据日期删除此日期的所有数据
    void delTrafficPeakStationDailyByDates(List<String> recordTime);
}
