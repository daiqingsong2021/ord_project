package com.wisdom.acm.dc2.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TrainDailyPassageMileVo
{
    /**
     * 主键ID
     */
    private String id;


    /**
     *线路
     */
    private String line;

    /**
     * 记录日期
     */
    private Date recordTime;

    /**
     *行车日况id
     */
    private String trainDailyId;
    /**
     *列次数
     */
    private String trainListNumber;
    /**
     *载客里程
     */
    private String passengerMiles;
    /**
     *空驶里程
     */
    private String emptyMiles;
    /**
     *既有线载客里程
     */
    private String allLinePassenageMiles;
    /**
     *既有线空驶里程
     */
    private String allLineEmptyMiles;
    /**
     *新线线载客里程
     */
    private String newLinePassenageMiles;
    /**
     *新线空驶里程
     */
    private String newLineEmptyMiles;
    /**
     *载客里程
     */
    private String abnormalPassengerMiles;
    /**
     *空驶里程
     */
    private String abnormalEmptyMiles;
    /**
     *既有线载客里程
     */
    private String abnormalAllPassenageMiles;
    /**
     *既有线空驶里程
     */
    private String abnormalAllEmptyMiles;
    /**
     *新线线载客里程
     */
    private String abnormalNewPassenageMiles;
    /**
     *新线空驶里程
     */
    private String abnormalNewEmptyMiles;

}
