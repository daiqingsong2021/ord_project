package com.wisdom.acm.dc5.po;

import com.wisdom.base.common.po.BasePo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "odr_fault_daily_problem")
@Data
public class FaultDailyProblemPo extends BasePo {

    /**
     *关联关系  odr_fault_daily表的主键
     */
    @Column(name = "fault_id")
    private Integer faultId;

    /**
     * 记录日期
     */
    @Column(name = "record_time")
    private Date recordTime;

    /**
     * 问题描述
     */
    @Column(name = "problem_desc")
    private String problemDesc;
    /**
     * 问题原因
     */
    @Column(name = "problem_reason")
    private String problemReason;

    /**
     *问题状态
     */
    @Column(name = "problem_status")
    private String problemStatus;

    /**
     * 备注
     */
    @Column(name = "problem_remark")
    private String remark;

    /**
     * 处理详情
     */
    @Column(name = "deal_detail")
    private String dealDetail;
}
