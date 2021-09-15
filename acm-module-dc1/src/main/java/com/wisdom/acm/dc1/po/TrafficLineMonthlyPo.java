package com.wisdom.acm.dc1.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 月累计线路客运量，按日汇总po
 */
@Table(name = "odr_traffic_line_monthly")
@Data
public class TrafficLineMonthlyPo extends BasePo {
    @Column(name = "record_time")
    private Date recordTime;
    @Column(name = "line")
    private String line;
    //线路分期
    @Column(name = "line_period")
    private String linePeriod;

    //月统计客运量
    @Column(name = "traffic_volume_month")
    private BigDecimal trafficVolumeMonth;

    //月日均客运量
    @Column(name = "traffic_volume_month_average")
    private BigDecimal trafficVolumeMonthAverage;

    //本月单日最大客流量
    @Column(name = "max_traffic_volume")
    private BigDecimal maxTrafficVolume;

    //本月单日最大客流量发生日期
    @Column(name = "max_traffic_date")
    private Date maxTrafficDate;
}
