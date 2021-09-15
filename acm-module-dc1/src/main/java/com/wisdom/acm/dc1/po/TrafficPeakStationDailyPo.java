package com.wisdom.acm.dc1.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 车站高峰时段客运量po
 */
@Table(name = "odr_traffic_peak_station_daily")
@Data
public class TrafficPeakStationDailyPo extends BasePo {
    @Column(name = "record_time")
    private Date recordTime;
    @Column(name = "line")
    private String line;
    //线路分期
    @Column(name = "line_period")
    private String linePeriod;

    //站
    @Column(name = "station")
    private String station;

    //早高峰进站量
    @Column(name = "morning_peak_arrival_volume")
    private BigDecimal morningPeakArrivalVolume;

    //早高峰出站量
    @Column(name = "morning_peak_outbound_volume")
    private BigDecimal morningPeakOutboundVolume;

    //早高峰客运量
    @Column(name = "morning_peak_traffic_volume")
    private BigDecimal morningPeakTrafficVolume;

    //晚高峰进站量
    @Column(name = "evening_peak_arrival_volume")
    private BigDecimal eveningPeakArrivalVolume;

    //晚高峰出站量
    @Column(name = "evening_peak_outbound_volume")
    private BigDecimal eveningPeakOutboundVolume;

    //晚高峰客运量
    @Column(name = "evening_peak_traffic_volume")
    private BigDecimal eveningPeakTrafficVolume;

    //高峰小时断面客流（上行）
    @Column(name = "transect_up_volume")
    private BigDecimal transectUpVolume;

    //高峰小时断面客流（下行）
    @Column(name = "transect_down_volume")
    private BigDecimal transectDownVolume;
}
