package com.wisdom.acm.dc1.mapper;

import com.wisdom.acm.dc1.po.TrafficLineTicketMonthlyPo;
import com.wisdom.acm.dc1.vo.TrafficLineTicketMonthlyVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TrafficLineTicketMonthlyMapper extends CommMapper<TrafficLineTicketMonthlyPo> {
    void delTrafficLineTicketMonthlyByDates(@Param("yearMonth") String yearMonth);

    void updateLineTicketMonthlyPo(@Param("line")String line, @Param("recordTime")String recordTime,
                                   @Param("ticketType")BigDecimal ticketType, @Param("rate")BigDecimal rate,
                                   @Param("trafficVolume")BigDecimal trafficVolume, @Param("totalTrafficVolume")BigDecimal totalTrafficVolume);

    List<TrafficLineTicketMonthlyVo> queryTrafficLineTicketMonthlyVo(@Param("yearMonth")String yearMonth);
}
