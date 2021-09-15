package com.wisdom.acm.dc1.service;

import com.wisdom.acm.dc1.po.TrafficLineTicketMonthlyPo;
import com.wisdom.acm.dc1.vo.TrafficLineTicketMonthlyVo;
import com.wisdom.base.common.service.CommService;

import java.util.List;

/**
 * @author zll
 * @date 2020/7/21/021 13:47
 * Description：<描述>
 */
public interface TrafficLineTicketMonthlyService extends CommService<TrafficLineTicketMonthlyPo> {
    //根据日期删除此日期的所有数据
    void delTrafficLineTicketMonthlyByDates(List<String> recordTime);

    void insertTrafficLineTicketMonthlys(List<TrafficLineTicketMonthlyPo> trafficLineTicketMonthlyPos);

    void updateLineTicketMonthlyPoList(List<TrafficLineTicketMonthlyPo> trafficLineTicketMonthlyPos);

    List<TrafficLineTicketMonthlyVo> queryTrafficLineTicketMonthlyVo(String yearMonth);

    void updateLineTicketMonthly(List<TrafficLineTicketMonthlyVo> listVos, List<TrafficLineTicketMonthlyPo> trafficLineTicketMonthlyPos);
}
