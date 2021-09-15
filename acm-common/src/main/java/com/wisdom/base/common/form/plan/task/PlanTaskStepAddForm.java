package com.wisdom.base.common.form.plan.task;

import com.wisdom.base.common.form.BaseForm;
import lombok.Data;

/**
 * @Author: szc
 * @Date: 2019/9/17 22:54
 * @Version 1.0
 */

@Data
public class PlanTaskStepAddForm extends BaseForm {

    //来源任务id
    private Integer sourceTaskId;

    //关联任务id
    private Integer relationTaskId;

    //名称
    private String name;

    //代码
    private String code;

    //设计总量
    private String totalDesign;

    //计量单位
    private String unit;

    //权重
    private double estwt;
}
