package com.wisdom.acm.dc1.mapper;

import com.wisdom.acm.dc1.po.TrafficLineTicketDailyPo;
import com.wisdom.acm.dc1.vo.TrafficLineTicketDailyVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

public interface TrafficLineTicketDailyMapper extends CommMapper<TrafficLineTicketDailyPo> {
    void delTrafficLineTicketDailyByDates(@Param("recordTimes") List<String> recordTimes);

    List<TrafficLineTicketDailyVo> queryTrafficLineTicketDailys(@Param("yearMonth")String yearMonth,@Param("recordTime")String recordTime);

    List<TrafficLineTicketDailyVo> queryLineTicketDailyList(Map<String, Object> mapWhere);
}
