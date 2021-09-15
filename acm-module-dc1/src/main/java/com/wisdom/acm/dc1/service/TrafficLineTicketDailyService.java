package com.wisdom.acm.dc1.service;

import com.github.pagehelper.PageInfo;
import com.wisdom.acm.dc1.po.TrafficLineTicketDailyPo;
import com.wisdom.acm.dc1.vo.TrafficLineTicketDailyVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;
import java.util.Map;

/**
 * @author zll
 * @date 2020/7/20/020 15:14
 * Description：<描述>
 */
public interface TrafficLineTicketDailyService extends CommService<TrafficLineTicketDailyPo> {
    //根据日期删除此日期的所有数据
    void delTrafficLineTicketDailyByDates(List<String> recordTime);

    void insertTrafficLineTicketDaily(List<TrafficLineTicketDailyPo> trafficLineTicketDailyPos);

    List<TrafficLineTicketDailyVo> queryTrafficLineTicketDailys(String yearMonth,String recordTime);

    List<TrafficLineTicketDailyVo> trafficLineTicketDailyVoList(String recordTime);

    PageInfo<TrafficLineTicketDailyVo> queryStationDailyList(Map<String, Object> mapWhere, Integer pageSize, Integer currentPageNum);

    void updateLineTicketDailyPos(List<TrafficLineTicketDailyPo> trafficLineTicketDailyPos,String date);

    List<TrafficLineTicketDailyVo> queryTicketDailyForReport(String recordTime, String line);
}
