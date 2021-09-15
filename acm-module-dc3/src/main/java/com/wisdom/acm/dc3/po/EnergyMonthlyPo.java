package com.wisdom.acm.dc3.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "odr_energy_monthly")
@Data
public class EnergyMonthlyPo extends BasePo
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
     * 当月总耗电量
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

}
