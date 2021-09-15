package com.wisdom.base.common.form.plan.change.pred;

import lombok.Data;

/**
 * @Author: szc
 * @Date: 2019/3/12 14:17
 * @Version 1.0
 */
@Data
public class PlanTaskPredChangeAddForm {

    //任务或者变更id
    private Integer taskId;

    //主任务是任务还是变更
    private String type;

    //被分配任务或者变更id
    private Integer predTaskId;

    //被分配任务是任务还是变更
    private String predType;

}
