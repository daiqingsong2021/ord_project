package com.wisdom.base.common.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author: szc
 * @Date: 2019/9/17 20:19
 * @Version 1.0
 */
@Table(name = "WSD_PLAN_STEP_TASK_RELATION")
@Data
public class PlanTaskStepRelationPo extends BasePo{

    @Column(name = "relation_taskId")
    private Integer relationTaskId;

    @Column(name = "source_taskId")
    private Integer sourceTaskId;

    @Column(name = "step_id")
    private Integer stepId;

    @Column(name="act_start_time")
    private Date actStartTime;

    @Column(name="act_end_time")
    private Date actEndTime;

    @Column(name="act_complete")
    private double actComplete;

    @Column(name="plan_complete")
    private double planComplete;

    @Column(name = "complete_pct")
    private String completePct;
}
