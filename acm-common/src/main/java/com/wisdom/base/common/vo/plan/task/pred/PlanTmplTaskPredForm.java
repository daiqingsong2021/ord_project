package com.wisdom.base.common.vo.plan.task.pred;

import lombok.Data;

@Data
public class PlanTmplTaskPredForm {
    //任务
    private Integer taskId;
    //紧前任务
    private Integer predTaskId;
    //关系类型
    private String relationType;
    //延时
    private Double lagQty;
}
