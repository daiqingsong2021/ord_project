package com.wisdom.acm.dc2.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "odr_train_daily_schedule")
@Data
public class TrainDailySchedulePo extends BasePo
{

    /**
     * 记录日期
     */
    @Column(name = "RECORD_TIME")
    private Date recordTime;
    /**
     * 线路类型（0：上行，1：下行）
     */
    @Column(name = "LINE_TYPE")
    private String lineType;

    /**
     * 类型编码
     */
    @Column(name = "TYPE_CODE")
    private String typeCode;

    /**
     * 类型名称
     */
    @Column(name = "TYPE_NAME")
    private String typeName;

    /**
     * 出段
     */
    @Column(name = "PERIOD")
    private String period;

    /**
     * 正线始发站
     */
    @Column(name = "START_STATION")
    private String startStation;

    /**
     * 终点站
     */
    @Column(name = "END_STATION")
    private String endStation;

    /**
     * 进段
     */
    @Column(name = "IN_STATION")
    private String inStation;

    /**
     *列数
     */
    @Column(name = "COLOUMNS")
    private String coloumns;
    /**
     *备注
     */
    @Column(name = "REMARK")
    private String remark;
    /**
     *里程
     */
    @Column(name = "MILEAGE")
    private String mileage;
    /**
     *线路
     */
    @Column(name = "LINE")
    private String line;
    /**
     *类型关系
     */
    @Column(name = "RELATION_NUMBER")
    private String relatinNumber;

    /**
     *行车日况id
     */
    @Column(name = "TRAIN_DAILY_ID")
    private String trainDailyId;

    //新线总里程
    @Column(name = "new_Line_Station_Mileage")
    private BigDecimal newLineStationMileage=new BigDecimal("0");
    //既有线总里程
    @Column(name = "old_Line_Station_Mileage")
    private BigDecimal oldLineStationMileage=new BigDecimal("0");
}
