package com.wisdom.acm.dc1.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 月累计车站进出站客运量，按日汇总po
 */
@Table(name = "odr_traffic_station_monthly")
@Data
public class TrafficStationMonthlyPo extends BasePo {
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

    //月累计进站量（万人次）
    @Column(name = "arrival_volume")
    private BigDecimal arrivalVolume;

    //月累计日均进站客流（万人次）
    @Column(name = "arrival_volume_average")
    private BigDecimal arrivalVolumeAverage;

    //月累计出站量（万人次）
    @Column(name = "outbound_volume")
    private BigDecimal outboundVolume;

    //月累计日均出站客流（万人次）
    @Column(name = "outbound_volume_average")
    private BigDecimal OutboundVolumeAverage;

    //月累计总进出站量（万人次）
    @Column(name = "total_volume")
    private BigDecimal totalVolume;

    //站点编号
    @Column(name = "station_num")
    private String stationNum;
}
