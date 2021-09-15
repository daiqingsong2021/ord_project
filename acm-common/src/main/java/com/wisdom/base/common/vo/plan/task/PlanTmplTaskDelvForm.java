package com.wisdom.base.common.vo.plan.task;

import lombok.Data;

import javax.print.attribute.IntegerSyntax;


@Data
public class PlanTmplTaskDelvForm {
    private Integer taskId;
    /**
     * 交付物id
     */
    private Integer delvId;
}
