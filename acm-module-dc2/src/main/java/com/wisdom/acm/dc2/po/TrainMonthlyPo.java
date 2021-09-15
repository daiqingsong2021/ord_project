package com.wisdom.acm.dc2.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "odr_train_monthly")
@Data
public class TrainMonthlyPo extends BasePo
{

    /**
     * 记录日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM")
    @Column(name = "RECORD_TIME")
    protected Date recordTime;
    /**
     *线路
     */
    @Column(name = "LINE")
    private String line;

    /**
     * 计划开行列
     */
    @Column(name = "PLAN_OPERATING_COLUMN")
    private String planOperatingColumn;
    /**
     * 实际开行列
     */
    @Column(name = "ACTUAL_OPERATING_COLUMN")
    private String actualOperatingColumn;

    /**
     * 兑现率
     */
    @Column(name = "FULFILLMENT_RATE")
    private String fulfillmentRate;

    /**
     * 准点率
     */
    @Column(name = "ON_TIME_RATE")
    private String onTimeRate;

    /**
     * 终到早点列
     */
    @Column(name = "ARRIVE_EARLY_COLUMN")
    private String arriveEarlyColumn;
    /**
     * 终到晚点列
     */
    @Column(name = "ARRIVE_LATE_COLUMN")
    private String arriveLateColumn;

    /**
     * 终到准点列
     */
    @Column(name = "ARRIVE_ONTIME_COLUMN")
    private String arriveOntimeColumn;

    /**
     *空驶里程
     */
    @Column(name = "DEADHEAD_KILOMETRES")
    private String deadheadKilometres;

    /**
     *载客里程
     */
    @Column(name = "CARRYING_KILOMETRES")
    private String carryingKilometres;
    /**
     *运营里程
     */
    @Column(name = "OPERATING_KILOMETRES")
    private String operatingKilometres;
    /**
     *2-5分钟始发晚点列次
     */
    @Column(name = "STARTING_LATE_COLUMN_2_5")
    private String startingLateColumn_2_5;
    /**
     *2-5分钟终到晚点列次
     */
    @Column(name = "ENDING_LATE_COLUMN_2_5")
    private String endingLateColumn_2_5;

    /**
     *5-10分钟始发晚点列次
     */
    @Column(name = "STARTING_LATE_COLUMN_5_10")
    private String startingLateColumn_5_10;

    /**
     *5-10分钟终到晚点列次
     */
    @Column(name = "ENDING_LATE_COLUMN_5_10")
    private String endingLateColumn_5_10;

    /**
     *10-15分钟始发晚点列次
     */
    @Column(name = "STARTING_LATE_COLUMN_10_15")
    private String startingLateColumn_10_15;


    /**
     *10-15分钟终到晚点列次
     */
    @Column(name = "ENDING_LATE_COLUMN_10_15")
    private String endingLateColumn_10_15;

    /**
     *15分钟以上始发晚点列次
     */
    @Column(name = "STARTING_LATE_COLUMN_15")
    private String startingLateColumn_15;
    /**
     *15分钟以上终到晚点列次
     */
    @Column(name = "ENDING_LATE_COLUMN_15")
    private String endingLateColumn_15;


    /**
     *加开（列）
     */
    @Column(name = "COLUMN_JK")
    private String columnJk;

    /**
     *救援（列）
     */
    @Column(name = "COLUMN_JY")
    private String columnJy;

    /**
     *跳停（列）
     */
    @Column(name = "COLUMN_TT")
    private String columnTt;

    /**
     *抽线（列）
     */
    @Column(name = "COLUMN_CX")
    private String columnCx;

    /**
     *下线（列）
     */
    @Column(name = "COLUMN_XX")
    private String columnXx;
    /**
     *清客（列）
     */
    @Column(name = "COLUMN_QK")
    private String columnQk;
    /**
     *运营（列）
     */
    @Column(name = "COLUMN_YY")
    private String columnYy;

    /**
     *描述
     */
    @Column(name = "DESCRIPTION")
    private String description;



}
