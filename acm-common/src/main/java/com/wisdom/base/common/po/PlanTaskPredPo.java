package com.wisdom.base.common.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 紧前任务
 */
@Table(name = "wsd_plan_taskpred")
@Data
public class PlanTaskPredPo extends BasePo {

    /**
     * 目标任务id
     */
    @Column(name = "define_id")
    private Integer defineId;

    /**
     * 任务id
     */
    @Column(name = "task_id")
    private Integer taskId;

    /**
     * 项目id
     */
    @Column(name = "project_id")
    private Integer projectId;

    /**
     * 目标任务id
     */
    @Column(name = "pred_define_id")
    private Integer predDefineId;

    /**
     * 目标任务id
     */
    @Column(name = "pred_task_id")
    private Integer predTaskId;

    /**
     * 关系
     */
    @Column(name = "relation_type")
    private String relationType;

    /**
     * 延时
     */
    @Column(name = "lag_num")
    private Double lagNum;

}
