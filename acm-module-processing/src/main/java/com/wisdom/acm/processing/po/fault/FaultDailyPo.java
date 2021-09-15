package com.wisdom.acm.processing.po.fault;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "odr_fault_daily")
@Data
public class FaultDailyPo extends BasePo {

    /**
     * 日期
     */
    @Column(name = "record_day")
    private Date recordDay;

    /**
     *线路
     */
    @Column(name = "line")
    private Integer line;


    /**
     * 车辆专业故障数
     */
    @Column(name = "major_vehicle")
    private Integer majorVehicle;

    /**
     * 供电专业故障数
     */
    @Column(name = "major_power")
    private Integer majorPower;

    /**
     * 信号专业故障数
     */
    @Column(name = "major_signal")
    private Integer majorSignal;

    /**
     * 通信专业故障数
     */
    @Column(name = "major_communication")
    private Integer majorCommunication;

    /**
     * 工建专业故障数
     */
    @Column(name = "major_construction")
    private Integer majorConstruction;

    /**
     *机电专业故障数
     */
    @Column(name = "major_mechatronics")
    private Integer majorMechatronics;

    /**
     *AFC专业故障数
     */
    @Column(name = "major_afc")
    private Integer majorAfc;
    /**
     *其他专业故障数
     */
    @Column(name = "major_other")
    private Integer majorOther;

    //'审批状态'
    @Column(name = "status")
    private String status;

    /**
     * 发起人ID
     */
    @Column(name = "initiator_id")
    private Integer initiatorId;

    /**
     * 发起时间
     */
    @Column(name = "init_time")
    private Date initTime;
    /**
     * 发起人
     */
    @Column(name = "initiator")
    private String initiator;

}
