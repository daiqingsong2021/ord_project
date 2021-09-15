package com.wisdom.acm.dc5.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "odr_fault_monthly")
@Data
public class FaultMonthlyPo extends BasePo {

    /**
     * 日期：年月的格式
     */
    @Column(name = "record_month")
    private String recordMonth;

    /**
     *线路
     */
    @Column(name = "LINE")
    private Integer line;


    /**
     * 车辆专业故障数
     */
    @Column(name = "major_vehicle")
    private Integer majorVehicle;

    /**
     * 供电专业故障数
     */
    @Column(name = "MAJOR_POWER")
    private Integer majorPower;

    /**
     * 信号专业故障数
     */
    @Column(name = "MAJOR_signal")
    private Integer majorSignal;

    /**
     * 通信专业故障数
     */
    @Column(name = "MAJOR_communication")
    private Integer majorCommunication;

    /**
     * 工建专业故障数
     */
    @Column(name = "MAJOR_construction")
    private Integer majorConstruction;

    /**
     *机电专业故障数
     */
    @Column(name = "MAJOR_Mechatronics")
    private Integer majorMechatronics;

    /**
     *AFC专业故障数
     */
    @Column(name = "MAJOR_AFC")
    private Integer majorAfc;
    /**
     *其他专业故障数
     */
    @Column(name = "MAJOR_OTHER")
    private Integer majorOther;
    /**
     *故障数合计
     */
    @Column(name = "TOTAL_FAULT")
    private Integer totalFault;

}
