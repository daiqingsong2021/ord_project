package com.wisdom.acm.dc2.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.acm.dc2.vo.TrainDailyPassageMileVo;
import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class TrainDailyUpdateForm extends BaseForm
{

    /**
     * 列车日况ID
     */
    @NotEmpty(message = "列车日况ID")
    private String id;

    //@JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    //private Date recordTime;
    /**
     *线路
     */
   // private String line;

    /**
     * 列车时刻表id
     */
    private String scheduleId;
    /**
     * 实际开行列
     */
   // @LogParam(title = "实际开行列")
    private String actualOperatingColumn;

    /**
     * 兑现率
     */
  //  @LogParam(title = "兑现率")
    private String fulfillmentRate;

    /**
     * 准点率
     */
   // @LogParam(title = "准点率")
    private String onTimeRate;

    /**
     * 终到早点列
     */
   // @LogParam(title = "终到早点列")
    private String arriveEarlyColumn;
    /**
     * 终到晚点列
     */
   // @LogParam(title = "终到晚点列")
    private String arriveLateColumn;

    /**
     * 终到准点列
     */
    //@LogParam(title = "终到准点列")
    private String arriveOntimeColumn;

    /**
     * 当天晚点
     */
    private String dayLate;

    /**
     *空驶里程
     */
    @LogParam(title = "空驶里程")
    private String deadheadKilometres;

    /**
     *载客里程
     */
    @LogParam(title = "载客里程")
    private String carryingKilometres;
    /**
     *运营里程
     */
    @LogParam(title = "运营里程")
    private String operatingKilometres;
    /**
     *2-5分钟终到早点列次
     */
    @LogParam(title = "2-5分钟终到早点列次")
    private String startingLateColumn_2_5;
    /**
     *2-5分钟终到晚点列次
     */
    @LogParam(title = "2-5分钟终到晚点列次")
    private String endingLateColumn_2_5;

    /**
     *5-10分钟终到早点列次
     */
    @LogParam(title = "5-10分钟终到早点列次")
    private String startingLateColumn_5_10;

    /**
     *5-10分钟终到晚点列次
     */
    @LogParam(title = "5-10分钟终到晚点列次")
    private String endingLateColumn_5_10;

    /**
     *10-15分钟终到早点列次
     */
    @LogParam(title = "10-15分钟终到早点列次")
    private String startingLateColumn_10_15;


    /**
     *10-15分钟终到晚点列次
     */
    @LogParam(title = "10-15分钟终到晚点列次")
    private String endingLateColumn_10_15;

    /**
     *15分钟以上始发晚点列次
     */
    @LogParam(title = "15分钟以上终到早点列次")
    private String startingLateColumn_15;
    /**
     *15分钟以上终点晚点列次
     */
    @LogParam(title = "15分钟以上终点晚点列次")
    private String endingLateColumn_15;


    /**
     *加开（列）
     */
    @LogParam(title = "加开（列）")
    private String columnJk;

    /**
     *救援（列）
     */
    @LogParam(title = "救援（列）")
    private String columnJy;

    /**
     *跳停（列）
     */
    @LogParam(title = "跳停（列）")
    private String columnTt;

    /**
     *抽线（列）
     */
    @LogParam(title = "抽线（列）")
    private String columnCx;

    /**
     *下线（列）
     */
    @LogParam(title = "下线（列）")
    private String columnXx;
    /**
     *清客（列）
     */
    @LogParam(title = "清客（列）")
    private String columnQk;
    /**
     *运营（列）
     */
    @LogParam(title = "运营（列）")
    private String columnYy;

    /**
     *描述
     */
    @LogParam(title = "描述")
    private String description;

    /**
     *使用车
     */
    @LogParam(title = "使用车")
    private String useCar;
    /**
     *备用车
     */
    @LogParam(title = "备用车")
    private String spareCar;
    /**
     *调试车
     */
    @LogParam(title = "调试车")
    private String debugCar;
    /**
     *检修车
     */
    @LogParam(title = "检修车")
    private String inspectionCar;
    /**
     *其它
     */
    @LogParam(title = "其它")
    private String other;

    /**
     *上行站点
     */
    private List<TrainDailyScheduleUpdateForm> trainDailyScheduleUpUpdateForm =new ArrayList<>();
    /**
     *下行站点
     */
    private List<TrainDailyScheduleUpdateForm> trainDailyScheduleDownUpdateForm  =new ArrayList<>();

    /**
     * 非正常里程
     */
    private TrainDailyPassageMileUpdateForm trainDailyPassageMileUpdateForm;
}
