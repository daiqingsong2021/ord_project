package com.wisdom.base.common.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author: szc
 * @Date: 2019/9/19 19:49
 * @Version 1.0
 */
@Table(name = "WSD_PLAN_STEP_FB")
@Data
public class PlanTaskStepFeedBackPo extends BasePo{

    @Column(name="feedback_id")
    private Integer feedbackId;

    @Column(name="project_id")
    private Integer projectId;

    @Column(name="define_id")
    private Integer defineId;

    @Column(name="task_id")
    private Integer taskId;

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
