package com.wisdom.acm.dc5.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class FaultDailyVo {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 日期
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date recordDay;

    private String recordDayStr;

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
     * 发起人ID
     */
    private Integer initiatorId;
    /**
     * 发起人
     */
    private String initiator;
    /**
     * 发起时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date initTime;
    /**
     *发布状态
     */
    private String status;
    /**
     *发布状态
     */
    private String statusDesc;
    /**
     *所有问题数量(所有专业的故障数的合计)
     */
    private Integer totalProblem;
    /**
     *新增问题数量
     */
    private Integer newProblem;
    /**
     *遗留问题数量
     */
    private Integer legacyProblem;

    /**
     *创建时间
     */
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date creatTime;

}
