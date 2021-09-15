package com.wisdom.acm.dc1.mapper;

import com.wisdom.acm.dc1.po.TrafficStationMonthlyPo;
import com.wisdom.acm.dc1.vo.TrafficStationMonthlyVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zll
 * @date 2020/7/20/020 13:22
 * Description：<描述>
 */
public interface TrafficStationMonthlyMapper extends CommMapper<TrafficStationMonthlyPo> {
    void delTrafficStationMonthlyByDates(@Param("yearMonth") String yearMonth);

    void updateTrafficStationMonthlyPoList(@Param("line")String line, @Param("recordTime")String recordTime,@Param("station")String station,
    @Param("arrivalVolume")BigDecimal arrivalVolume, @Param("arrivalVolumeAverage")BigDecimal arrivalVolumeAverage,
    @Param("outboundVolume")BigDecimal outboundVolume,@Param("outboundVolumeAverage")BigDecimal outboundVolumeAverage,@Param("totalVolume")BigDecimal totalVolume);

    List<TrafficStationMonthlyVo> queryTrafficStationMonthly(@Param("yearMonth")String yearMonth);
}
