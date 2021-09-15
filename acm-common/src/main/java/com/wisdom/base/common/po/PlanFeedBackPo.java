package com.wisdom.base.common.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "wsd_plan_feedback")
@Data
public class PlanFeedBackPo extends BasePo {

    //任务
    @Column(name = "task_id")
    private Integer taskId;

    //计划定义id
    @Column(name = "define_id")
    private Integer defineId;

    //项目id
    @Column(name = "project_id")
    private Integer projectId;

    //实际开始时间
    @Column(name = "act_start_time")
    private Date actStartTime;

    //实际结束时间
    @Column(name = "act_end_time")
    private Date actEndTime;

    //报告时间
    @Column(name = "reporting_time")
    private Date reportingTime;

    //截止时间
    @Column(name = "deadline")
    private Date deadline;

    //估计完成
    @Column(name = "estimated_time")
    private Date estimatedTime;

    //申请完成%
    @Column(name = "complete_pct")
    private Double completePct;

    //进展说明
    @Column(name = "remark")
    private String remark;

    //状态
    @Column(name = "status")
    private String status;

}
