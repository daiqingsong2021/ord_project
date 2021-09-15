package com.wisdom.acm.processing.service.traffic;

import com.wisdom.acm.processing.vo.traffic.TrafficLineDailyVo;
import java.util.List;

/**
 * @author zll
 * 2020/8/17/017 13:18
 * Description:<描述>
 */
public interface TrafficLineDailyService {
    List<TrafficLineDailyVo> trafficLineDailyVoList(String recordTime, String line);
}
