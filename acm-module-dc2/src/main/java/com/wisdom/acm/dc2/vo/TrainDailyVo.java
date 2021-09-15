package com.wisdom.acm.dc2.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class TrainDailyVo
{

    /**
     *主键
     */
    private String id;
    /**
     * 记录日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    protected Date recordTime;
    /**
     *线路
     */
    private String line;

    /**
     * 列车时刻表id
     */
    private String scheduleId;
    /**
     * 实际开行列
     */
    private String actualOperatingColumn;

    /**
     * 兑现率
     */
    private String fulfillmentRate;

    /**
     * 准点率
     */
    private String onTimeRate;

    /**
     * 终到早点列
     */
    private String arriveEarlyColumn;
    /**
     * 终到晚点列
     */
    private String arriveLateColumn;

    /**
     * 终到准点列
     */
    private String arriveOntimeColumn;

    /**
     * 当天晚点
     */
    private String dayLate;

    /**
     *空驶里程
     */
    private String deadheadKilometres;

    /**
     *载客里程
     */
    private String carryingKilometres;
    /**
     *运营里程
     */
    private String operatingKilometres;
    /**
     *2-5分钟始发晚点列次
     */
    private String startingLateColumn_2_5;
    /**
     *2-5分钟终到晚点列次
     */
    private String endingLateColumn_2_5;

    /**
     *5-10分钟始发晚点列次
     */
    private String startingLateColumn_5_10;

    /**
     *5-10分钟终到晚点列次
     */
    private String endingLateColumn_5_10;

    /**
     *10-15分钟始发晚点列次
     */
    private String startingLateColumn_10_15;


    /**
     *10-15分钟终到晚点列次
     */
    private String endingLateColumn_10_15;

    /**
     *15分钟以上始发晚点列次
     */
    private String startingLateColumn_15;
    /**
     *15分钟以上终点晚点列次
     */
    private String endingLateColumn_15;


    /**
     *加开（列）
     */
    private String columnJk;

    /**
     *救援（列）
     */
    private String columnJy;

    /**
     *跳停（列）
     */
    private String columnTt;

    /**
     *抽线（列）
     */
    private String columnCx;

    /**
     *下线（列）
     */
    private String columnXx;
    /**
     *清客（列）
     */
    private String columnQk;
    /**
     *运营（列）
     */
    private String columnYy;
    /**
     *审批人
     */
    private String reviewer;
    /**
     *审批状态
     */
    private String reviewStatus;
    /**
     *描述
     */
    private String description;

    private Date reviewTime;

    /**
     * 创建日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    protected Date creatTime;
    /**
     * 创建人
     */
    private GeneralVo createrVo = new GeneralVo();
    /**
     * 列车时刻表
     */
    private TrainScheduleVo trainScheduleVo=new TrainScheduleVo();

    /**
     *使用车
     */
    private String useCar;
    /**
     *备用车
     */
    private String spareCar;
    /**
     *调试车
     */
    private String debugCar;
    /**
     *检修车
     */
    private String inspectionCar;
    /**
     *其它
     */
    private String other;
    /**
     *其它
     */
    private String lineName;

    /**
     * 状态
     */
    private GeneralVo reviewStatusVo = new GeneralVo();



    /**
     *上行站点
     */
    private List<TrainDailyScheduleVo> trainDailyUpSchedule =new ArrayList<>();
    /**
     *下行站点
     */
    private List<TrainDailyScheduleVo> trainDailyDownSchedule  =new ArrayList<>();
    /**
     * 非正常里程
     */
    private TrainDailyPassageMileVo trainDailyPassageMileVo;
}
