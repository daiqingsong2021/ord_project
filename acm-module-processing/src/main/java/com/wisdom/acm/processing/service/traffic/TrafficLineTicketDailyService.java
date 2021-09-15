package com.wisdom.acm.processing.service.traffic;

import com.wisdom.acm.processing.po.traffic.TrafficLineTicketDailyPo;
import com.wisdom.acm.processing.vo.traffic.TrafficLineTicketDailyVo;
import com.wisdom.base.common.service.CommService;
import java.util.List;

/**
 * @author zll
 * 2020/8/17/017 13:41
 * Description:<描述>
 */
public interface TrafficLineTicketDailyService extends CommService<TrafficLineTicketDailyPo> {
    List<TrafficLineTicketDailyVo> queryTicketDailyForReport(String recordTime, String line);
}
