package com.wisdom.acm.dc1.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 车站进出站客运量po
 */
@Table(name = "odr_traffic_station_daily")
@Data
public class TrafficStationDailyPo extends BasePo {
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

    //进站量
    @Column(name = "arrival_volume")
    private BigDecimal arrivalVolume;

    //出站量
    @Column(name = "outbound_volume")
    private BigDecimal outboundVolume;

    //进站量+出站量
    @Column(name = "total_volume")
    private BigDecimal totalVolume;
    //站点编号
    @Column(name = "station_num")
    private String stationNum;
}
