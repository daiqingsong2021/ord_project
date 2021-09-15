package com.wisdom.acm.processing.po.traffic;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 线路客运量，按车站汇总po
 */
@Table(name = "odr_traffic_line_daily")
@Data
@EqualsAndHashCode(callSuper=false)
public class TrafficLineDailyPo extends BasePo {
    @Column(name = "record_time")
    private Date recordTime;
    @Column(name = "line")
    private String line;

    //线路分期
    @Column(name = "line_period")
    private String linePeriod;

    //进站量
    @Column(name = "arrival_volume")
    private BigDecimal arrivalVolume;

    //出站量
    @Column(name = "outbound_volume")
    private BigDecimal outboundVolume;

    //换入量
    @Column(name = "transfer_volume")
    private BigDecimal transferVolume;

    //进站量+换入量
    @Column(name = "traffic_volume")
    private BigDecimal trafficVolume;


    //月累计客运量（万人次）
    @Column(name = "traffic_volume_month")
    private BigDecimal trafficVolumeMonth;

    //本月日均客运量（万人次）
    @Column(name = "traffic_volume_month_average")
    private BigDecimal trafficVolumeMonthAverage;

    //年累计客运量（万人次）
    @Column(name = "traffic_volume_year")
    private BigDecimal trafficVolumeYear;

    //年累计日均客运量（万人次）
    @Column(name = "traffic_volume_year_average")
    private BigDecimal trafficVolumeYearAverage;

    //开通累计客运量（万人次）
    @Column(name = "traffic_volume_open")
    private BigDecimal trafficVolumeOpen;

    //开通累计日均客运量（万人次）
    @Column(name = "traffic_volume_open_average")
    private BigDecimal trafficVolumeOpenAverage;
}
