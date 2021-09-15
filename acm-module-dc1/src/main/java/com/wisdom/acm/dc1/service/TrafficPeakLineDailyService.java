package com.wisdom.acm.dc1.service;

import com.wisdom.acm.dc1.po.TrafficPeakLineDailyPo;
import com.wisdom.acm.dc1.vo.TrafficPeakLineDailyVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;

public interface TrafficPeakLineDailyService extends CommService<TrafficPeakLineDailyPo> {
    //根据日期删除此日期的所有数据
    void delTrafficPeakLineDailyByDates(List<String> recordTime);

    void insertTrafficPeakLineDaily(TrafficPeakLineDailyPo trafficPeakLineDailyPo);

    List<TrafficPeakLineDailyVo> trafficStationDailyVos(String recordTime);

    void updatePeakLineDailyPos(List<TrafficPeakLineDailyPo> trafficPeakLineDailyPos);

    List<TrafficPeakLineDailyVo> queryPeakLineForReport(String recordTime, String line);

    List<TrafficPeakLineDailyVo> queryTrafficPeakLineDailyVos(String yearMonth);
}
