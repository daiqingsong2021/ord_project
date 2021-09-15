package com.wisdom.acm.dc2.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.acm.dc2.vo.TrainScheduleStationVo;
import com.wisdom.base.common.aspect.LogParam;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class TrainDailyScheduleUpdateForm
{

    /**
     * 主键ID
     */
    @NotEmpty(message = "非正常线路录入id")
    private String id;
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
     *类型关系
     */
    private String relatinNumber;

    /**
     *行车日况id
     */
    private String trainDailyId;

    //新线总里程
    private BigDecimal newLineStationMileage=new BigDecimal("0");
    //既有线总里程
    private BigDecimal oldLineStationMileage=new BigDecimal("0");
}
