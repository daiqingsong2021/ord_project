package com.wisdom.acm.processing.service.traffic;

import com.wisdom.acm.processing.po.traffic.TrafficPeakLineDailyPo;
import com.wisdom.acm.processing.vo.traffic.TrafficPeakLineDailyVo;
import com.wisdom.base.common.service.CommService;
import java.util.List;

/**
 * @author zll
 * 2020/8/17/017 13:51
 * Description:<描述>
 */
public interface TrafficPeakLineDailyService extends CommService<TrafficPeakLineDailyPo> {
    List<TrafficPeakLineDailyVo> queryPeakLineForReport(String recordTime, String line);
}
