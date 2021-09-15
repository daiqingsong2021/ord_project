package com.wisdom.acm.dc1.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc1.po.TrafficLineDailyPo;
import com.wisdom.acm.dc1.vo.TrafficLineDailyVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

public interface  TrafficLineDailyService extends CommService<TrafficLineDailyPo> {
    PageInfo<TrafficLineDailyVo> queryTrafficLineList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);

    void insertTrafficLines(List<TrafficLineDailyPo> list);

    //根据日期删除此日期的所有数据
    void delTrafficLineByDates(List<String>recordTime);

    List<TrafficLineDailyVo> trafficLineDailyVoList(String recordTime);

    PageInfo<TrafficLineDailyVo> queryTrafficLineNetWorkList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);

    void updateTrafficLineDailyPos(List<TrafficLineDailyPo>list,Integer id);

    List<TrafficLineDailyVo> queryMaxTrafficVolumeList(Map<String, Object> mapWhere);
}
