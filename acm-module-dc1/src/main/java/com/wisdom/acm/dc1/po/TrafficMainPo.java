package com.wisdom.acm.dc1.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zll
 * @date 2020/7/20/020 11:04
 * Description：<客运量日况主表po>
 */
@Table(name = "odr_traffic_main")
@Data
public class TrafficMainPo extends BasePo {
    @Column(name = "record_time")
    private Date recordTime;

    //'本日客运量（人次）'
    @Column(name = "traffic_volume_today")
    private BigDecimal trafficVolumeToday;

    //'月累计客运量（万人次）'
    @Column(name = "traffic_volume_month")
    private BigDecimal trafficVolumeMonth;

    //'本月日均客运量（万人次）'
    @Column(name = "traffic_volume_month_average")
    private BigDecimal trafficVolumeMonthAverage;

    //'年累计客运量（万人次）'
    @Column(name = "traffic_volume_year")
    private BigDecimal trafficVolumeYear;

    //'年累计日均客运量（万人次）'
    @Column(name = "traffic_volume_year_average")
    private BigDecimal trafficVolumeYearAverage;

    //'开通累计客运量（万人次）'
    @Column(name = "traffic_volume_open")
    private BigDecimal trafficVolumeOpen;

    //'开通累计日均客运量（万人次）'
    @Column(name = "traffic_volume_open_average")
    private BigDecimal trafficVolumeOpenAverage;

    //'审批状态'
    @Column(name = "status")
    private String status;

    /**
     * 发起人ID
     */
    @Column(name = "initiator_id")
    private Integer initiatorId;

    /**
     * 发起时间
     */
    @Column(name = "init_time")
    private Date initTime;
    /**
     * 发起人
     */
    @Column(name = "initiator")
    private String initiator;
}
