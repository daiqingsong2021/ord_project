package com.wisdom.base.common.vo.plan.task;

import lombok.Data;

import java.util.Date;

/**
 * @Author: szc
 * @Date: 2019/9/21 14:11
 * @Version 1.0
 */
@Data
public class PlanTaskStepFeedBackVo {

    private Integer feedbackId;

    private Integer projectId;

    private Integer defineId;

    private Integer taskId;

    private Integer stepId;

    private Date actStartTime;

    private Date actEndTime;

    private Integer actComplete;

    private Integer planComplete;

    private String completePct;
}
