package com.wisdom.acm.dc5.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wisdom.base.common.vo.GeneralVo;
import lombok.Data;

import java.util.Date;

@Data
public class FaultMonthlyVo {
    /**
     * 主键ID
     */
    private String id;

    /**
     * 日期
     */
    private String recordMonth;

    /**
     *线路
     */
    private Integer line;
    /**
     *线路
     */
    private String lineName;

    /**
     * 车辆专业故障数
     */
    private Integer majorVehicle;

    /**
     * 供电专业故障数
     */
    private Integer majorPower;

    /**
     * 信号专业故障数
     */
    private Integer majorSignal;

    /**
     * 通信专业故障数
     */
    private Integer majorCommunication;

    /**
     * 工建专业故障数
     */
    private Integer majorConstruction;

    /**
     *机电专业故障数
     */
    private Integer majorMechatronics;

    /**
     *AFC专业故障数
     */
    private Integer majorAfc;
    /**
     *其他专业故障数
     */
    private Integer majorOther;
    /**
     *故障数合计
     */
    private Integer totalFault;

    /**
     * 创建人
     */
    private GeneralVo createVo = new GeneralVo();

    /**
     *创建时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date creatTime;

}
