package com.wisdom.acm.dc1.service;

import com.wisdom.acm.dc1.po.TrafficPeakStationMonthlyPo;
import com.wisdom.acm.dc1.po.TrafficStationMonthlyPo;
import com.wisdom.base.common.service.CommService;

import java.util.List;

public interface TrafficPeakStationMonthlyService extends CommService<TrafficPeakStationMonthlyPo> {
    //根据日期删除此日期的所有数据
    void delTrafficPeakStationMonthlyByDates(List<String> recordTime);

}
