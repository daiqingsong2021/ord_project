package com.wisdom.acm.dc2.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.acm.dc2.vo.TrainScheduleStationVo;
import com.wisdom.base.common.aspect.LogParam;
import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

import java.util.Date;

@Data
public class TrainScheduleStationAddForm extends BaseForm
{



    /**
     * 线路类型（0：上行，1：下行）
     */
    @LogParam(title = "线路类型")
    private String lineType;

    /**
     * 类型编码
     */
    private String typeCode;

    /**
     * 类型名称
     */
    @LogParam(title = "类型名称")
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

    /**
     *是否为标题行
     */
    private String rowType;
}
