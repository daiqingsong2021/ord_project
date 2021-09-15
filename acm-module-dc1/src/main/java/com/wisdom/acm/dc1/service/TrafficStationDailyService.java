package com.wisdom.acm.dc1.service;


import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc1.po.TrafficStationDailyPo;
import com.wisdom.acm.dc1.vo.TrafficStationDailyVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface TrafficStationDailyService extends CommService<TrafficStationDailyPo> {
    //根据日期删除此日期的所有数据
    void delTrafficStationDailyByDates(List<String> recordTime);

    void insertStationDaily(List<TrafficStationDailyPo> list);

    List<TrafficStationDailyVo> queryTrafficStationDailys(String date);

    List<TrafficStationDailyVo> trafficStationDailyVos(String recordTime);

    PageInfo<TrafficStationDailyVo> queryStationDailyList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);

    void updateStationDailyPos(List<TrafficStationDailyPo> trafficStationDailyPos,String date);
}
