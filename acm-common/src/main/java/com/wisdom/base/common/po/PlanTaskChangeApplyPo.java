package com.wisdom.base.common.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;


/**
 * @Author: szc
 * @Date: 2019/3/28 14:44
 * @Version 1.0
 */
@Data
@Table(name = "wsd_plan_taskchangeapply")
public class PlanTaskChangeApplyPo extends BasePo {


    //变更编号
    @Column(name = "change_num")
    private String changeNum;

    //变更申请原因
    @Column(name = "change_apply_reason")
    private String changeApplyReason;

    //变更影响
    @Column(name = "change_effect")
    private String changeEffect;

    //流程id
    @Column(name = "workflow_id")
    private int workflowId;

    //采取措施说明
    @Column(name = "change_way_desc")
    private String changeWayDesc;
}
