package com.wisdom.acm.dc2.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class TrainScheduleVo
{
    /**
     * 主键ID
     */
    private String id;

    /**
     * 列车时刻表名称
     */
    private String scheduleName;

    /**
     * 列车时刻表编码
     */
    private String scheduleCode;

    /**
     * 最大上线列车数
     */
    private String maxOnlineTrain;

    /**
     * 最小行车间隔
     */
    private String minDrivingInterval;

    /**
     * 备用车数
     */
    private String standbyTrain;

    /**
     * 计划开行列
     */
    private String plannedOperationColumn;

    /**
     * 状态
     */
    private GeneralVo reviewStatusVo = new GeneralVo();
    /**
     * 创建人
     */
    private GeneralVo createrVo = new GeneralVo();

    /**
     *说明
     */
    private String description;


    /**
     *线路
     */
    private String line;
    /**
     *线路
     */
    private String lineName;
    /**
     *始发站
     */
    private String startStation;
    /**
     *始发站
     */
    private String startStationName;
    /**
     *末端站
     */
    private String endStation;
    /**
     *末端站
     */
    private String endStationName;

    /**
     *首班车时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="HH:mm:ss")
    private Date startDriveTime;
    /**
     *末班车时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="HH:mm:ss")
    private Date endDriveTime;

    /**
     * 创建日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    protected Date creatTime;

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
     *上行站点
     */
    private List<TrainScheduleStationVo> trainScheduleUpStationVo =new ArrayList<>();
    /**
     *下行站点
     */
    private List<TrainScheduleStationVo> trainScheduleDownStationVo  =new ArrayList<>();


}
