package com.wisdom.acm.dc3.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "odr_energy_daily")
@Data
public class EnergyDailyPo extends BasePo
{

    /**
     * 日期
     */
    @Column(name = "RECORD_TIME")
    private Date recordTime;

    /**
     *线路
     */
    @Column(name = "LINE")
    private String line;


    /**
     * 逗号分隔：一期,二期,三期
     */
    @Column(name = "LINE_PERIOD")
    private String linePeriod;

    /**
     * 当日动照
     */
    @Column(name = "MOTIVE_POWER_CONSUMPTION")
    private String motivePowerConsumption;

    /**
     * 当日牵引
     */
    @Column(name = "TOW_POWER_CONSUMPTION")
    private String towPowerConsumption;

    /**
     * 当日损耗
     */
    @Column(name = "LOSS_POWER_CONSUMPTION")
    private String lossPowerConsumption;

    /**
     * 当日总耗电量
     */
    @Column(name = "TOTAL_POWER_CONSUMPTION")
    private String totalPowerConsumption;

    /**
     *动照占比
     */
    @Column(name = "MOTIVE_POWER_RATE")
    private String motivePowerRate;

    /**
     *牵引占比
     */
    @Column(name = "TOW_POWER_RATE")
    private String towPowerRate;
    /**
     *损耗占比
     */
    @Column(name = "LOSS_POWER_RATE")
    private String lossPowerRate;
    /**
     *审批人
     */
    @Column(name = "REVIEWER")
    private Integer reviewer;
    /**
     *审批状态
     */
    @Column(name = "REVIEW_STATUS")
    private String reviewStatus;
    /**
     *审批时间
     */
    @Column(name = "REVIEW_TIME")
    private Date reviewTime;

}
