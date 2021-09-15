package com.wisdom.acm.dc1.service;


import com.wisdom.acm.dc1.po.TrafficStationMonthlyPo;
import com.wisdom.acm.dc1.vo.TrafficStationMonthlyVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;

public interface TrafficStationMonthlyService extends CommService<TrafficStationMonthlyPo> {
    //根据日期删除此日期的所有数据
    void delTrafficStationMonthlyByDates(List<String> recordTime);

    void insertStationMonthly(List<TrafficStationMonthlyPo> list);

    void updateTrafficStationMonthlyPoList(List<TrafficStationMonthlyPo> trafficStationMonthlyPoList);

    List<TrafficStationMonthlyVo> queryTrafficStationMonthly(String yearMonth);

    void updateTrafficStationMonthly(List<TrafficStationMonthlyVo> listVos, List<TrafficStationMonthlyPo> insertTrafficStationMonthlys);
}
