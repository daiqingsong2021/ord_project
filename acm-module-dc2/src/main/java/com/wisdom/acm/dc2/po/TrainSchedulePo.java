package com.wisdom.acm.dc2.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "odr_train_schedule")
@Data
public class TrainSchedulePo extends BasePo
{

    /**
     * 列车时刻表名称
     */
    @Column(name = "SCHEDULE_NAME")
    private String scheduleName;

    /**
     * 列车时刻表编码
     */
    @Column(name = "SCHEDULE_CODE")
    private String scheduleCode;

    /**
     * 最大上线列车数
     */
    @Column(name = "MAX_ONLINE_TRAIN")
    private String maxOnlineTrain;

    /**
     * 最小行车间隔
     */
    @Column(name = "MIN_DRIVING_INTERVAL")
    private String minDrivingInterval;

    /**
     * 备用车数
     */
    @Column(name = "STANDBY_TRAIN")
    private String standbyTrain;

    /**
     * 计划开行列
     */
    @Column(name = "PLANNED_OPERATION_COLUMN")
    private String plannedOperationColumn;

    /**
     * 状态
     */
    @Column(name = "REVIEW_STATUS")
    private String reviewStatus;

    /**
     *说明
     */
    @Column(name = "DESCRIPTION")
    private String description;
    /**
     *线路
     */
    @Column(name = "LINE")
    private String line;
    /**
     *始发站
     */
    @Column(name = "START_STATION")
    private String startStation;
    /**
     *末端站
     */
    @Column(name = "END_STATION")
    private String endStation;
    /**
     *首班车时间
     */
    @Column(name = "START_DRIVE_TIME")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="HH:mm:ss")
    private Date startDriveTime;
    /**
     *末班车时间
     */
    @Column(name = "END_DRIVE_TIME")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="HH:mm:ss")
    private Date endDriveTime;

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









}
