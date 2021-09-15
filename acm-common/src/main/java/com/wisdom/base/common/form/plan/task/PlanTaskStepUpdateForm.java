package com.wisdom.base.common.form.plan.task;

import lombok.Data;

/**
 * @Author: szc
 * @Date: 2019/9/18 10:54
 * @Version 1.0
 */

@Data
public class PlanTaskStepUpdateForm {

    //id
    private Integer id;

    //名称
    private String name;

    //设计总量
    private String totalDesign;

    //计量单位
    private String unit;

    //权重
    private double estwt;

}
