package com.wisdom.acm.dc2.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "odr_train_daily_mile")
@Data
public class TrainDailyPassageMilePo extends BasePo
{


    /**
     *线路
     */
    @Column(name = "LINE")
    private String line;

    /**
     * 记录日期
     */
    @Column(name = "RECORD_TIME")
    private Date recordTime;

    /**
     *行车日况id
     */
    @Column(name = "TRAIN_DAILY_ID")
    private String trainDailyId;
    /**
     *列次数
     */
    @Column(name = "TRAIN_LIST_NUMBER")
    private String trainListNumber;
    /**
     *载客里程
     */
    @Column(name = "PASSENGER_MILES")
    private String passengerMiles;
    /**
     *空驶里程
     */
    @Column(name = "EMPTY_MILES")
    private String emptyMiles;
    /**
     *既有线载客里程
     */
    @Column(name = "ALL_LINE_PASSENAGE_MILES")
    private String allLinePassenageMiles;
    /**
     *既有线空驶里程
     */
    @Column(name = "ALL_LINE_EMPTY_MILES")
    private String allLineEmptyMiles;
    /**
     *新线线载客里程
     */
    @Column(name = "NEW_LINE_PASSENAGE_MILES")
    private String newLinePassenageMiles;
    /**
     *新线空驶里程
     */
    @Column(name = "NEW_LINE_EMPTY_MILES")
    private String newLineEmptyMiles;
    /**
     *载客里程
     */
    @Column(name = "ABNORMAL_PASSENGER_MILES")
    private String abnormalPassengerMiles;
    /**
     *空驶里程
     */
    @Column(name = "ABNORMAL_EMPTY_MILES")
    private String abnormalEmptyMiles;
    /**
     *既有线载客里程
     */
    @Column(name = "ABNORMAL_ALL_PASSENAGE_MILES")
    private String abnormalAllPassenageMiles;
    /**
     *既有线空驶里程
     */
    @Column(name = "ABNORMAL_ALL_EMPTY_MILES")
    private String abnormalAllEmptyMiles;
    /**
     *新线线载客里程
     */
    @Column(name = "ABNORMAL_NEW_PASSENAGE_MILES")
    private String abnormalNewPassenageMiles;
    /**
     *新线空驶里程
     */
    @Column(name = "ABNORMAL_NEW_EMPTY_MILES")
    private String abnormalNewEmptyMiles;


}
