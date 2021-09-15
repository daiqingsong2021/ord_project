package com.wisdom.acm.dc3.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "odr_energy_detail")
@Data
public class EnergyDetailPo extends BasePo
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
     * 0：主变电所；1：动照；2：牵引
     */
    @Column(name = "POWER_TYPE")
    private String powerType;

    /**
     * 变电所/车站
     */
    @Column(name = "SUBSTATION")
    private String subStation;

    /**
     * 开关柜
     */
    @Column(name = "SWITCHGEAR")
    private String switchgear;

    /**
     * 耗电量
     */
    @Column(name = "POWER_CONSUMPTION")
    private String powerConsumption;

    /**
     *描述
     */
    @Column(name = "DESCRIPTION")
    private String description;
    /**
     *日况id
     */
    @Column(name = "DAILY_ID")
    private String dailyId;


}
