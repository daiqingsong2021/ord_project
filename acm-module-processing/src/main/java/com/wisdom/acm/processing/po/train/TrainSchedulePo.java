package com.wisdom.acm.processing.po.train;

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

}
