package com.wisdom.acm.dc1.service.impl;

import com.wisdom.acm.dc1.mapper.TrafficPeakStationMonthlyMapper;
import com.wisdom.acm.dc1.po.TrafficPeakStationMonthlyPo;
import com.wisdom.base.common.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.wisdom.acm.dc1.service.TrafficPeakStationMonthlyService;

import java.util.List;

/**
 * 实现类
 */
@Slf4j
@Service
public class TrafficPeakStationMonthlyServiceImpl extends BaseService<TrafficPeakStationMonthlyMapper, TrafficPeakStationMonthlyPo>
        implements TrafficPeakStationMonthlyService {
    @Override
    public void delTrafficPeakStationMonthlyByDates(List<String> recordTime) {
        mapper.delTrafficPeakStationMonthlyByDates(recordTime);
    }
}
