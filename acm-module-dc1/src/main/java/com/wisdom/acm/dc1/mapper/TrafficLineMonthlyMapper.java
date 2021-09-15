package com.wisdom.acm.dc1.mapper;

import com.wisdom.acm.dc1.po.TrafficLineMonthlyPo;
import com.wisdom.acm.dc1.vo.TrafficLineMonthlyVo;
import com.wisdom.base.common.mapper.CommMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author zll
 * @date 2020/7/21/021 13:50
 * Description：<描述>
 */
public interface TrafficLineMonthlyMapper extends CommMapper<TrafficLineMonthlyPo> {
    void delTrafficLineMonthlyByDates(@Param("yearMonth")String yearMonth);

    void updateTrafficLineMonthlyPo(@Param("line")String line, @Param("recordTime")String recordTime,
                                    @Param("trafficVolumeMonth")BigDecimal trafficVolumeMonth,
                                    @Param("trafficVolumeMonthAverage")BigDecimal trafficVolumeMonthAverage);

    List<TrafficLineMonthlyVo> queryTrafficLineMonthlyVo(Map<String, Object> mapWhere);

    void updateTrafficLineMonthlyByMonthly(@Param("line")String line, @Param("yearMonth")String yearMonth,
                                           @Param("trafficVolumeMonth")BigDecimal trafficVolumeMonth,
                                           @Param("trafficVolumeMonthAverage")BigDecimal trafficVolumeMonthAverage,
                                           @Param("maxTrafficDate") Date maxTrafficDate,
                                           @Param("maxTrafficVolume")BigDecimal maxTrafficVolume);
}
