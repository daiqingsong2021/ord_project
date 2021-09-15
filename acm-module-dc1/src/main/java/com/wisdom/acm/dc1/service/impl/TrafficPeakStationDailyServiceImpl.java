package com.wisdom.acm.dc1.service.impl;

import com.wisdom.acm.dc1.mapper.TrafficPeakStationDailyMapper;
import com.wisdom.acm.dc1.po.TrafficPeakStationDailyPo;
import com.wisdom.acm.dc1.service.TrafficPeakStationDailyService;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 实现类
 */
@Slf4j
@Service
public class TrafficPeakStationDailyServiceImpl extends BaseService<TrafficPeakStationDailyMapper, TrafficPeakStationDailyPo>
        implements TrafficPeakStationDailyService {
    @Override
    public void delTrafficPeakStationDailyByDates(List<String> recordTime) {
        mapper.delTrafficPeakStationDailyByDates(recordTime);
    }
}
