package com.wisdom.base.common.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author: szc
 * @Date: 2019/9/20 10:02
 * @Version 1.0
 */
@Table(name = "WSD_PLAN_STEP_TASK_COMPLETE")
@Data
public class PlanTaskStepCompletePo extends BasePo{

    @Column(name="task_id")
    private Integer taskId;

    @Column(name="feedback_id")
    private Integer feedBackId;

    @Column(name = "step_id")
    private Integer stepId;

    @Column(name="act_start_time")
    private Date actStartTime;

    @Column(name="act_end_time")
    private Date actEndTime;

    @Column(name="act_complete")
    private Integer actComplete;

    @Column(name="plan_complete")
    private Integer planComplete;

    @Column(name = "complete_pct")
    private String completePct;
}
