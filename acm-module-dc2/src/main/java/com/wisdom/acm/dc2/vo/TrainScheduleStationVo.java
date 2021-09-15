package com.wisdom.acm.dc2.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class TrainScheduleStationVo
{
    /**
     * 主键ID
     */
    private String id;


    /**
     * 线路类型（0：上行，1：下行）
     */
    private String lineType;

    /**
     * 类型编码
     */
    private String typeCode;

    /**
     * 类型名称
     */
    private String typeName;

    /**
     * 出段
     */
    private String period;

    /**
     * 正线始发站
     */
    private String startStation;

    /**
     * 终点站
     */
    private String endStation;

    /**
     * 进段
     */
    private String inStation;

    /**
     *列数
     */
    private String coloumns;
    /**
     *备注
     */
    private String remark;
    /**
     *里程
     */
    private String mileage;

    /**
     *线路
     */
    private String line;
    /**
     *线路
     */
    private String lineName;
    /**
     *时刻表编码
     */
    private String scheduleCode;
    /**
     *时刻表ID
     */
    private String scheduleId;


    /**
     *类型关系
     */
    private String relatinNumber;

    //新线总里程
    private BigDecimal newLineStationMileage=new BigDecimal("0");
    //既有线总里程
    private BigDecimal oldLineStationMileage=new BigDecimal("0");

    /**
     *是否为标题行
     */
    private String rowType;
}
