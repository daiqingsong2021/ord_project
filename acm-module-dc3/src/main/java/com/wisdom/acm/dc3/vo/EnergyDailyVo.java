package com.wisdom.acm.dc3.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import java.util.Date;

@Data
public class EnergyDailyVo
{
    /**
     * 主键ID
     */
    private String id;

    /**
     * 日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private String recordTime;

    /**
     *线路
     */
    private String line;
    /**
     *线路
     */
    private String lineName;


    /**
     * 逗号分隔：一期,二期,三期
     */
    private String linePeriod;

    /**
     * 当日动照
     */
    private String motivePowerConsumption;

    /**
     * 当日牵引
     */
    private String towPowerConsumption;

    /**
     * 当日损耗
     */
    private String lossPowerConsumption;

    /**
     * 当日总耗电量
     */
    private String totalPowerConsumption;

    /**
     *动照占比
     */
    private String motivePowerRate;

    /**
     *牵引占比
     */
    private String towPowerRate;
    /**
     *损耗占比
     */
    private String lossPowerRate;
    /**
     *审批人
     */
    private String reviewer;
    /**
     *审批时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date reviewTime;
    /**
     * 状态
     */
    private GeneralVo reviewStatusVo = new GeneralVo();
    /**
     * 创建人
     */
    private GeneralVo createVo = new GeneralVo();
    /**
     * 审批人
     */
    private GeneralVo reviewerVo = new GeneralVo();

    /**
     *说明
     */
    private String description;
    /**
     *类型说明说明
     */
    private String powerTypeName;
    /**
     *类型
     */
    private String powerType;
    /**
     *能耗
     */
    private String powerConsumption;
    /**
     *创建时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date creatTime;




}
